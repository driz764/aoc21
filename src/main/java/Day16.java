import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) throws IOException {
        // Parse
        var data = "E20D7880532D4E551A5791BD7B8C964C1548CB3EC1FCA41CC00C6D50024400C202A65C00C20257C008AF70024C00810039C00C3002D400A300258040F200D6040093002CC0084003FA52DB8134DE620EC01DECC4C8A5B55E204B6610189F87BDD3B30052C01493E2DC9F1724B3C1F8DC801E249E8D66C564715589BCCF08B23CA1A00039D35FD6AC5727801500260B8801F253D467BFF99C40182004223B4458D2600E42C82D07CC01D83F0521C180273D5C8EE802B29F7C9DA1DCACD1D802469FF57558D6A65372113005E4DB25CF8C0209B329D0D996C92605009A637D299AEF06622CE4F1D7560141A52BC6D91C73CD732153BF862F39BA49E6BA8C438C010E009AA6B75EF7EE53BBAC244933A48600B025AD7C074FEB901599A49808008398142013426BD06FA00D540010C87F0CA29880370E21D42294A6E3BCF0A080324A006824E3FCBE4A782E7F356A5006A587A56D3699CF2F4FD6DF60862600BF802F25B4E96BDD26049802333EB7DDB401795FC36BD26A860094E176006A0200FC4B8790B4001098A50A61748D2DEDDF4C6200F4B6FE1F1665BED44015ACC055802B23BD87C8EF61E600B4D6BAD5800AA4E5C8672E4E401D0CC89F802D298F6A317894C7B518BE4772013C2803710004261EC318B800084C7288509E56FD6430052482340128FB37286F9194EE3D31FA43BACAF2802B12A7B83E4017E4E755E801A2942A9FCE757093005A6D1F803561007A17C3B8EE0008442085D1E8C0109E3BC00CDE4BFED737A90DC97FDAE6F521B97B4619BE17CC01D94489E1C9623000F924A7C8C77EA61E6679F7398159DE7D84C015A0040670765D5A52D060200C92801CA8A531194E98DA3CCF8C8C017C00416703665A2141008CF34EF8019A080390962841C1007217C5587E60164F81C9A5CE0E4AA549223002E32BDCEA36B2E100A160008747D8B705C001098DB13A388803F1AE304600";
        String binData = data.chars().mapToObj(c -> hexToBin((char) c)).collect(Collectors.joining());

        Packet packet = readPacket(binData);
        System.out.println("Day 16, part 1 : " + part1);
        System.out.println("Day 16, part 2 : " + packet.getVal());
    }

    static int cursor = 0;
    static int part1 = 0;

    public static Packet readPacket(String binData) {
        var version = readSimpleInt(binData);
        var typeId = readSimpleInt(binData);
        part1 += version;
        if (typeId == 4) {
            var resultString = "";
            do {
                resultString += binData.substring(cursor + 1, cursor + 5);
                cursor += 5;
            } while (binData.charAt(cursor - 5) != '0');
            return new LiteralPacket(version, Long.parseLong(resultString, 2));
        }
        List<Packet> result = new ArrayList<>();
        var mode = binData.charAt(cursor++);
        if (mode == '0') {
            var length = Integer.parseInt(binData.substring(cursor, cursor + 15), 2);
            cursor += 15;
            int end = cursor + length;
            while (cursor < end)
                result.add(readPacket(binData));
        } else {
            var nbSubPacket = Integer.parseInt(binData.substring(cursor, cursor + 11), 2);
            cursor += 11;
            for (int i = 0; i < nbSubPacket; i++) {
                result.add(readPacket(binData));
            }
        }
        return new OperatorPacket(version, typeId, result);
    }

    public static int readSimpleInt(String binData) {
        var result = Integer.parseInt(binData.substring(cursor, cursor + 3), 2);
        cursor += 3;
        return result;
    }

    public static String hexToBin(char c) {
        return switch (c) {
            case '0' -> "0000";
            case '1' -> "0001";
            case '2' -> "0010";
            case '3' -> "0011";
            case '4' -> "0100";
            case '5' -> "0101";
            case '6' -> "0110";
            case '7' -> "0111";
            case '8' -> "1000";
            case '9' -> "1001";
            case 'A' -> "1010";
            case 'B' -> "1011";
            case 'C' -> "1100";
            case 'D' -> "1101";
            case 'E' -> "1110";
            case 'F' -> "1111";
            default -> throw new RuntimeException("Unparseable char");
        };
    }

    static abstract class Packet {
        int version;

        public Packet(int version) {
            this.version = version;
        }

        abstract long getVal();
    }

    static class OperatorPacket extends Packet {
        List<Packet> subpackets;
        int type;

        public OperatorPacket(int version, int type, List<Packet> result) {
            super(version);
            this.type = type;
            this.subpackets = result;
        }

        @Override
        long getVal() {
            return switch (type) {
                case 0 -> subpackets.stream().mapToLong(Packet::getVal).sum();
                case 1 -> subpackets.stream().mapToLong(Packet::getVal).reduce(1, (a, b) -> a * b);
                case 2 -> subpackets.stream().mapToLong(Packet::getVal).min().getAsLong();
                case 3 -> subpackets.stream().mapToLong(Packet::getVal).max().getAsLong();
                case 5 -> subpackets.get(0).getVal() > subpackets.get(1).getVal() ? 1L : 0L;
                case 6 -> subpackets.get(0).getVal() < subpackets.get(1).getVal() ? 1L : 0L;
                case 7 -> subpackets.get(0).getVal() == subpackets.get(1).getVal() ? 1L : 0L;
                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }
    }

    static class LiteralPacket extends Packet {
        long val;

        public LiteralPacket(int version, long val) {
            super(version);
            this.val = val;
        }

        @Override
        long getVal() {
            return val;
        }
    }

}

/*

 */