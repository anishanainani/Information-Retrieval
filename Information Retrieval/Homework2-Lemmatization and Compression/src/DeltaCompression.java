
public class DeltaCompression {
	public static byte[] deltaEncoding(Integer number) {
        String numBinary = Integer.toBinaryString(number);
        String offset = numBinary.substring(1, numBinary.length());

        byte gammaCode[] = GammaCompression.gammaEncoding(numBinary.length());
        String deltaCode = "";
        for (int i = 0; i < gammaCode.length; i++) {
            if (gammaCode[i] == (byte)1) {
                deltaCode = deltaCode + 1;
            } else {
                deltaCode = deltaCode + 0;
            }
        }
        deltaCode = deltaCode + offset;
        byte byteArray[] = new byte[deltaCode.length()];

        char char_array[] = deltaCode.toCharArray();
        for (int i = 0; i < char_array.length; i++) {
            if (char_array[i] == '1') {
            	byteArray[i] = (byte) 1;
            } else {
            	byteArray[i] = (byte) 0;
            }
        }

        return byteArray;
    }
}
