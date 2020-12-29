import javax.lang.model.util.ElementScanner6;

public class Car{
  private double mpg;
  private double gas;

  public Car(double theMpg, double theGas){
    mpg = theMpg;
    gas = theGas;
  }

  public double getMpg(){
    return mpg;
  }

  public double getGas(){
    return gas;
  }

  public void setMpg(double theMpg){
    mpg = theMpg;
  }

  public void setGas(double theGas){
    gas = theGas;
  }

public String toString(){
   String output = "The car gets "+ mpg + " miles per gallon.\nThe car has " + gas + " gallons left.";

   return output;
}

public boolean equals(Object obj){
  Car other = (Car) obj;
  boolean first = Math.abs(mpg-other.mpg)<0.01;
  boolean second = Math.abs(gas-other.gas)<0.01;
  return first && second;
}

public double drive(double mi) {
        double maxDistance = gas * mpg;
        if (maxDistance < mi) { 
            gas = 0;
            return maxDistance;
        } else {  
            gas = gas - mi/mpg;
            return mi;
        }
    }





}
