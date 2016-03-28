
public class GammaCompression {
	public static byte[] gammaEncoding(Integer number) {
        String numBinary = Integer.toBinaryString(number);
        String offset = numBinary.substring(1, numBinary.length());
        
        StringBuilder unary = new StringBuilder();
        for (int i = 0; i < offset.length(); i++) {
            unary.append(1);
        }
        unary.append(0);
        
        String gammaCode = unary + offset;
                
        char gammaArray[] = gammaCode.toCharArray();
        byte byteArray[] = new byte[gammaArray.length];
        
        for(int  i = 0; i<gammaArray.length;i++){
            if(gammaArray[i] == '1'){
            	byteArray[i] = (byte)1;
            }else{
            	byteArray[i] = (byte)0;
            }
        }
        
        return byteArray;
    }
}
