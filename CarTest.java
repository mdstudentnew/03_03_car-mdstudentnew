import java.lang.reflect.*;

/**
 * This class tests a class
 * 
 * @author Mr. Aronson
 */
public class CarTest
{
    public static void main(String[] args) {
        CarTest p = new CarTest();
        p.test();
    }

    String className = "Car";
    String[] fieldNames = {"mpg", "gas"};
    String[] fieldTypes = {"double", "double"};
    String[] fieldValues = {"20.0", "10.0"};
    boolean[] fieldFinal = {false, false};
    boolean[] fieldInit = {false, false};
    Object[] cArgs = {20.0, 10.0};
    String toStringValid = "The car gets 20.0 miles per gallon.\nThe car has 10.0 gallons left.";
    private  boolean failed = false;
    private  Object t1;
    private  Class<?> c;
    private Constructor hConstructor;

    private int getFieldIndex(String f) 
    {
        for (int i = 0; i < fieldNames.length; i++) {
            if (f.contains(fieldNames[i]))
                return i;
        }
        return -1;
    }

    private void checkField(Field field)
    {
        String temp = field.toString();
        int index = getFieldIndex(temp);
        if (index < 0) {
            failure("Not a valid instance variable: "+ temp);
            return;
        }
        if (!temp.contains(fieldTypes[index]))
            failure(fieldNames[index] + " instance variable is not a " + fieldTypes[index]);
        else if (!fieldFinal[index] && !temp.contains("private"))
            failure(fieldNames[index] + " instance variable is not private");
        else if (fieldFinal[index] && !temp.contains("final"))
            failure(fieldNames[index] + " instance variable should be final");
        else if (fieldValues[index].length() > 0) {

            try
            {
                field.setAccessible(true);
                if (fieldTypes[index].equals("double") && 
                Math.abs((double)field.getDouble(t1)) - Double.parseDouble(fieldValues[index]) < .001)
                    fieldInit[index] = true;
                else if (fieldTypes[index].equals("int") && 
                field.getInt(t1) == Integer.parseInt(fieldValues[index]))
                    fieldInit[index] = true;

                else
                    failure("not setting " + fieldNames[index] + " correctly");
            }
            catch (IllegalAccessException e)
            {
                failure("not setting " + fieldNames[index] + " correctly");
            }
        }
    }

    public  boolean test()
    {
        //**********  Class Test **************************************
        // Instantiate a new object
        System.out.println("Testing your " + className + " class: \n");

        if (!failed)    testConstructor();
        if (!failed)    testInstanceVariables();
        if (!failed)    testGetMethods();
        if (!failed)    testSetMethods();
        if (!failed)    testToString();
        if (!failed)    testEquals();
        if (!failed)    testOtherMethods();

        if(!failed)
        {
            System.out.println("Congratulations, your " + className + " class works correctly \n");
            System.out.println("****************************************************\n");
        }

        if (!failed)
            testStation();
        if(!failed)
            System.out.println("Yay! You have successfully completed the Car project!");
        else
            System.out.println("\nBummer.  Try again.");
        return !failed;
    }

    private void testConstructor() {
        System.out.println("Testing constructor");
        try
        {
            c = Class.forName(className);
            hConstructor = c.getConstructor(new Class[] {double.class, double.class});
            t1 = hConstructor.newInstance(cArgs);

        }
        catch (NoClassDefFoundError e)
        {
            failure("Epic Failure: missing " + className + " class");
        }
        catch (ClassNotFoundException e)
        {
            failure("Epic Failure: missing " + className + " class");
        }
        catch (NoSuchMethodException e)
        {
            failure("missing needed constructor :" + e.toString());
        }
        catch (Exception e)
        {
            failure(e.toString());
        }

        if(!failed)
            System.out.println("Passed  constructor test\n");

    }

    private void testInstanceVariables() {
        System.out.println("Testing instance variables");
        if(!failed)
        {
            Field[] fields = c.getDeclaredFields();
            if(fields.length == 0)
                failure("Class has no instance variables");
            else
            {
                for(Field field : fields)
                {
                    if (failed)
                        continue;
                    checkField(field);
                }
            }
        }

        if (!failed) {
            for (int i = 0; i < fieldInit.length; i++ ) {
                if (fieldValues[i].length() > 0 && !fieldInit[i])
                    failure(fieldNames[i] + " instance variable not set to proper value");
            }
        }

        if(!failed)
            System.out.println("Passed  instance variable test\n");

    }

    private void testGetMethods() {
        System.out.println("Testing get methods");

        try {
            if (!failed) {
                Object obj = hConstructor.newInstance(cArgs);

                for (int i = 0; i < fieldNames.length; i++) {
                    if (!fieldFinal[i]) {
                        String str = fieldNames[i];
                        str = "get" + str.substring(0,1).toUpperCase() + str.substring(1);
                        Method m = c.getDeclaredMethod(str);
                        Object tempNum = m.invoke(obj);
                        if (fieldValues[i].length() > 0) {
                            if (fieldTypes[i].equals("double") ) {

                                if (Math.abs((Double)tempNum - Double.parseDouble(fieldValues[i])) > .001)
                                    failure(str + " not getting proper value " + fieldValues[i]);

                            } else if (fieldTypes[i].equals("int")) {

                                if ((Integer)tempNum != Integer.parseInt(fieldValues[i]))
                                    failure(str + " not getting proper value " + fieldValues[i]);
                            }
                        }
                    }
                }
                /*
                Method s = c.getDeclaredMethod("setWeight", new Class[] {double.class});
                args = new Object[] {12.0};
                s.invoke(obj, args);
                 */

            }
        } catch (NoSuchMethodException e) {
            failure(e.toString());
        } catch (InstantiationException e) {
            failure(e.toString());
        } catch (IllegalAccessException e) {
            failure(e.toString());
        } catch (InvocationTargetException e) {
            failure(e.toString());
        }

        if(!failed)
            System.out.println("Passed  get methods test\n");
    }

    private void testSetMethods() {
        System.out.println("Testing set methods");
        try {

            // test setMpg
            if (!failed) {
                System.out.println("Testing setMpg method");

                Object[] tempArgs = {8.0, 7.0};
                Object obj = hConstructor.newInstance(tempArgs);
                Method m = c.getDeclaredMethod("setMpg", double.class);
                m.invoke(obj, 9.0);

                m = c.getDeclaredMethod("getMpg");
                double tempNum = (Double)m.invoke(obj);
                if (Math.abs(tempNum - 9.0) > .0001)
                    failure("setMpg changed mpg to " + tempNum + " instead of 9.0");
            }
            if(!failed)
                System.out.println("Passed  setMpg method test\n");

            // test setElectricity
            if (!failed) {
                System.out.println("Testing setGas method");
                Object[] tempArgs = {8.0, 7.0};
                Object obj = hConstructor.newInstance(tempArgs);
                Method m = c.getDeclaredMethod("setGas", double.class);
                m.invoke(obj, 9.0);
                Method e = c.getDeclaredMethod("getGas");
                double tempNum = (Double)e.invoke(obj);
                if (Math.abs(tempNum - 9.0) > .0001)
                    failure("setGas changed gas to " + tempNum + " instead of 9.0");
            }
            if(!failed)
                System.out.println("Passed  setGas method test\n");
        } catch (Exception e) {
            failure(e.toString());
        }
        if(!failed)
            System.out.println("Passed  set methods test\n");

    }

    private void testOtherMethods() {
        System.out.println("Testing other methods");
        try {

            if (!failed) {
                double tempNum;
                Object c1 = hConstructor.newInstance(cArgs);
                Object c2 = hConstructor.newInstance(new Object[] {10.0, 5.0});
                Method d = c.getDeclaredMethod("drive", double.class);
                Method g = c.getDeclaredMethod("getGas");;
                String c1str = c1.toString();
                String c2str = c2.toString();
                if((tempNum = (Double)d.invoke(c1, 100.0)) != 100.0)
                    failure("\n" + c1str + "\nshould have been able to drive 100 miles instead of " + tempNum);
                else if (Math.abs((Double)g.invoke(c1) - 5.0) > .001)
                    failure("\n" + c1str + "\nshould have only used 5.0 gallons to drive 100 miles but has " + (double)g.invoke(c1) + " left");
                else if ((tempNum = (Double)d.invoke(c2, 1000.0)) != 50.0)
                    failure("\n" + c2str + "\nshould only have been able to drive 50.0 miles instead of " + tempNum);
                else if ((Double)g.invoke(c2) > .000001)
                    failure("\n" + c2str + "\nshould be out of gas after driving 50.0 miles but has " + (Double)g.invoke(c2) + " left");    
            }
        } catch (Exception e) {
            failure (e.toString());
        }
        if(!failed)
            System.out.println("Passed  other methods test\n");

    }

    private void testToString() {
        if(!failed)
        {
            System.out.println("Testing toString method");
            String objectToString = t1.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(t1));
            if(t1.toString().equals(objectToString))
                failure("missing toString method");
        }

        if(!failed)
        {
            if(!t1.toString().equals(toStringValid))
                failure("toString is invalid: \n" + t1.toString() + "\nshould be:\n" + toStringValid );
        }

        if(!failed)
            System.out.println("Passed  toString method test\n");
    }

    private void testEquals() {
        if(!failed)
        {
            System.out.println("Testing equals method");
            try
            {
                Constructor constructor = c.getConstructor(new Class[] {double.class, double.class});
                Object temp1 = constructor.newInstance(new Object[] {20.0, 10.0});
                Object temp2 = constructor.newInstance(new Object[] {10.0, 10.0});
                Object temp3 = constructor.newInstance(new Object[] {20.0, 20.0});
                if(!t1.equals(temp1))
                    failure("\n" + t1.toString() + "\nshould equal\n" + temp1.toString() + "\n(maybe missing equals method?)");
                else if (t1.equals(temp2))
                    failure("\n" + t1.toString() + "\nshould not equal\n" + temp2.toString() );
                else if (t1.equals(temp3))
                    failure("\n" + t1.toString() + "\nshould not equal\n" + temp3.toString() );
                else if (!t1.equals(constructor.newInstance(new Object[] {20.00000000000001, 10.0})))
                    failure("Car with 20.0 mpg should equal car with 20.00000000000001 mpg");
                else if (!t1.equals(constructor.newInstance(new Object[] {20.0, 10.00000000000001})))
                    failure("Car with 10.0 gas should equal car with 10.00000000000001 gas");
                else if (t1.equals(constructor.newInstance(new Object[] {21.0, 10.0})))
                    failure("Car with 20.0 mpg should not equal car with 21.0 mpg");
                else if (t1.equals(constructor.newInstance(new Object[] {20.0, 11.0})))
                    failure("Car with 10.0 gas should not equal car with 11.0 mpg");

            }
            catch (Exception e)
            {
                failure(e.toString());
            }
        }
        if (!failed) 
            System.out.println("Passed  equals method test\n");
    }

    private void testStation() 
    {
        //********** Station Class Test **************************************
        // Instantiate a new Station object
        System.out.println("\nTesting your Station class: \n");
        Object s1 = null;
        Class s = null;
        System.out.println("Testing Station constructor");
        try
        {

            s = Class.forName("Station");
            Constructor con = s.getConstructor();
            s1  = con.newInstance();

        }
        catch (NoClassDefFoundError e)
        {
            failure("Epic Failure: missing Station class");
        }
        catch (ClassNotFoundException e)
        {
            failure("Epic Failure: missing Station class");
        }
        catch (Exception e)
        {
            failure(e.toString());
        }

        if(!failed)
            System.out.println("Passed  Station constructor test\n");

        if(!failed)
        {
            System.out.println("Testing fuel method");
            try
            {
                Method f = s.getDeclaredMethod("fuel", new Class[] {c, double.class});
                Method g = c.getDeclaredMethod("getGas");
                Constructor cConstructor = c.getConstructor(new Class[] {double.class, double.class});
                Object car1 = cConstructor.newInstance(new Object[] {20.0, 10.0});
                double result = (Double)f.invoke(s1, car1, 5.0);
                                double val = (Double)g.invoke(car1);
                if(Math.abs(val-15.0) > .0001)
                    failure("Car getGas returned " + val + " but should have been 15.0");

                if(!failed && Math.abs(result-15.0) > .0001)
                    failure("Car fuel method should have returned 15.0 but returned " + result);

            }
            catch (NoSuchMethodException e) {
                failure("Missing double fuel(Car c, double gas) method");
                System.out.println(e);
            }
            catch (Exception e)
            {
                failure(e.toString());
            }
        }
        if(!failed)
            System.out.println("Passed  fuel method test\n");


        if(!failed)
        {
            System.out.println("Congratulations, your Station class works correctly \n");
            System.out.println("****************************************************\n");
        }
    }

    private  void failure(String str)
    {
        System.out.println("*** Failed: " + str);
        failed = true;
    }

}
