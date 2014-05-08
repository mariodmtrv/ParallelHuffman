
public class Main {
	public static void main(String[] args) throws Exception {
		FrequencyTable ftg = new FrequencyTable("test.txt");
		int[] buffer= ftg.generateFrequencyMap(0, 24);
		for(int ind=0; ind<buffer.length;ind++){
			if(buffer[ind]>0){
			System.out.println(buffer[ind]);
			}
		}

	}
}
