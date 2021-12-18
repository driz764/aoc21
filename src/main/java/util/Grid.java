package util;

import java.util.Iterator;

public class Grid<T> implements Iterable<Pair<Vector, T>> {
    private final T[][] grid;
    private final int sizeX;
    private final int sizeY;

    public Grid(T[][] grid) {
        this.grid = grid;
        sizeX = grid.length;
        sizeY = grid[0].length;
    }

    @Override
    public Iterator<Pair<Vector, T>> iterator() {
        return new GridIterator<>(this);
    }

    private static class GridIterator<T> implements Iterator<Pair<Vector, T>> {
        private final Grid<T> grid;
        private int cursor = 0;

        public GridIterator(Grid<T> grid) {
            this.grid = grid;
        }

        @Override
        public boolean hasNext() {
            return cursor < grid.sizeX * grid.sizeY;
        }

        @Override
        public Pair<Vector, T> next() {
            var position = new Vector(cursor / grid.sizeX, cursor % grid.sizeY);
            cursor++;
            return new Pair<>(position, grid.grid[position.x()][position.y()]);
        }
    }

    private static class GridNbIterator<T> implements Iterator<Pair<Vector, T>> {
        private final Grid<T> grid;
        private final Vector position;
        private int cursor = 0;

        public GridNbIterator(Grid<T> grid, Vector pos) {
            this.grid = grid;
            this.position = pos;
        }

        @Override
        public boolean hasNext() {
            return cursor < grid.sizeX * grid.sizeY;
        }

        @Override
        public Pair<Vector, T> next() {
            var position = new Vector(cursor / grid.sizeX, cursor % grid.sizeY);
            cursor++;
            return new Pair<>(position, grid.grid[position.x()][position.y()]);
        }
    }
}
