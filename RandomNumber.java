import java.util.Random;

public class RandomNumber {
	
	int max;
	int min;
	
	public RandomNumber(int max, int min){
		this.max=max;
		this.min=min;
	}
	public int randWeights(){
		Random ran = new Random();
		boolean v = ran.nextInt(9)==0;
		int rand = ran.nextInt((max-min)+1)+min;
		if(v){
			return 0;
		}
		else{
			return rand;
		}
	}
	
	public int randInitial(){
		Random ran = new Random();
		int rand = ran.nextInt((max-min)+1)+min;
		return rand;
	}

}
