import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
class KeyValueRow{
    String key;
    String value;
    
    KeyValueRow(String key, String value)
    {
        this.key = key;
        this.value = value;
    }
}
    
public class KeyValueStore{
        // Actual Data store
        Map<String,KeyValueRow> data = new HashMap<>();
        
        // Helper Datastructure for NumEqualTo
        Map<String, Integer> valueTracker = new HashMap<>();
        
        public void SET(String key, String value)
        {
            KeyValueRow row = new KeyValueRow(key, value);
            data.put(key, row);
            valueTracker.put(value,valueTracker.getOrDefault(value,0) + 1);
        }
        
        public void UNSET(String key)
        {
            KeyValueRow kvr = data.get(key);
            valueTracker.put(kvr.value,valueTracker.get(kvr.value) - 1);
            data.remove(key);
        }
        
        public String GET(String key)
        {
            if(data.containsKey(key))
            {
                KeyValueRow row = data.get(key);
                return row.value;
            }
            return "null";    
        }
        
        public void END()
        {
            System.exit(0);
        }
        
        
        public int NUMEQUALTO(String value)
        {
            if(valueTracker.containsKey(value))
            {
                int val = valueTracker.get(value);
                return val;
            }
            return 0;
        }
    
    public static void main(String args[]) {
        KeyValueStore kvs = new KeyValueStore();
        
        try{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 
        // Reading data using readLine
        String command ;
        // Using stack to implement nested begin's.
        Stack<Character> stack = new Stack<Character>();
        // Printing the read line
        while ((command = reader.readLine()) != null) 
        {
             
            String[] arguments = command.split(" ");
            
            // Two hashmaps for begin blocks.
            // These will be later used to copy to the main hashmaps of KeyValueStore, if committed.
            Map<String,KeyValueRow> beginsRowTracker = new HashMap<>();
            Map<String, Integer> beginsValueTracker = new HashMap<>();
            
            if(arguments[0].equals("BEGIN"))
            {
                 stack.push('B');// B for begin.
            }
            else if(arguments[0].equals("GET"))
            {
                System.out.println(kvs.GET(arguments[1]));   
            }
            else if(arguments[0].equals("SET"))
            {
                if(!stack.isEmpty())
                {
                    beginsRowTracker.put(arguments[1], new KeyValueRow(arguments[1],arguments[2]));
                    beginsValueTracker.put(arguments[2], beginsValueTracker.getOrDefault(arguments[2],0) + 1);
                }
                else
                    kvs.SET(arguments[1],arguments[2]);
            }
            else if(arguments[0].equals("UNSET"))
            {
                kvs.UNSET(arguments[1]);
            }
            else if(arguments[0].equals("NUMEQUALTO"))
            {
                System.out.println(kvs.NUMEQUALTO(arguments[1]));
            }
            else if(arguments[0].equals("END"))
            {
                kvs.END();
            }
            else if(arguments[0].equals("COMMIT"))   
            {
                if(!stack.isEmpty())
                {
                    stack.pop();
                    for(Map.Entry<String, KeyValueRow> e : beginsRowTracker.entrySet())
                    {
                        kvs.SET(e.getKey(), e.getValue().value);
                    }
                }
                else
                    System.out.println("Invalid command"); // A commit without a begin.
            }
            else if(arguments[0].equals("ROLLBACK"))   
            {
                if(!stack.isEmpty())
                    stack.pop();
                else
                    System.out.println("Invalid command"); // A rollback without a begin.
            }
        }
        if(!stack.isEmpty())
            System.out.println("Caution : Open block");
      }
      catch(IOException e)
      {
        System.out.println("invalid command");  
      }
    }
}
