[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=3855142&assignment_repo_type=AssignmentRepo)
  
# Car Lab

1.  In the class ```Car```, add a ```double mpg``` to represent the car’s miles per gallon 
      and a ```double gas``` to represent the car’s gas tank capacity in gallons.

2.  Add a constructor that sets the mpg and gas through two parameters.

3.  Add accessor methods  ```public double getMpg()```  and  ```public double getGas()``` .

4.  Add mutator methods  ```public void setMpg(double theMPG)``` and  ```public void setGas(double theGas)```. 

5.  Add a ```public String toString()``` method that is formatted exactly like the following (in this example, for a car with 10.0 mpg and 5.0 gallons left):
    The car gets 10.0 miles per gallon.
    The car has 5.0 gallons left.

6.  Add method:

        boolean equals(Object obj)
    
    to show that this car and obj are equal if they have the same mpg and same gas.
      
    Hint: The first line of this method should look something like:
      
        Car other = (Car) obj;

    so you can access the instance variables of obj.

    Hint: Comparing doubles cannot be just done with == 

7.  Add method ```drive(double mi)``` which drives the car mi miles or some distance until it runs out of gas. 
      It should return the actual distance traveled.

8. In the class ```Station```, add method
            double fuel(Car c, double gas)
      that adds the given gas amount to the given car and returns the total amount of gas that the car now has.


