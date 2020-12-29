

public class Station{
  public double fuel(Car c, double gas){
    double currentGas = c.getGas();
    c.setGas(currentGas + gas); 
    return c.getGas();

  }


}
