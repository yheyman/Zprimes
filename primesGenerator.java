import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Yuri Heymann 10/07/2021
 * Prime number generator
 * based on the main sequence (odd numbers)
 */
public class primesGenerator {
	
	// array to hold the sequence of primes
	ArrayList<Integer> primeArray = new ArrayList<Integer>();
	
	String fullpath = "c:\\temp\\Zprimes.txt";
	
	/* retrieves some element from main sequence
	 * indexed by integer n
	 */
	private int getMainSequenceElement(int n)
	{
		return 3 + 2*n;
	}
	 
	/* returns the index corresponding to an
	 * element of the main sequence
	 */
	private int getIndexAtElement(int element)
	{
		return (element - 3)/2;
	}
	
	/*
	 * returns the largest element of the sequence 
	 * which is smaller or equal to a real number x
	 */
    public int getSigma(double x)
    {
        int val = (int)((x - 3.0) / 2.0);
        return val;
    }
	 
    /* returns the sequence of the first N primes 
     * piecewise linear extraction by modulo function
	 */
	private ArrayList<Integer> getFirstNprimes(int N)
	{
		// array to hold the sequence of primes
		ArrayList<Integer> primeSequence = new ArrayList<Integer>();
		
		// array to hold the sequence of modulo primes for pruning
		ArrayList<Integer> moduloSequence = new ArrayList<Integer>();
		
		// initialise the arrays
        primeSequence.add(3);
        primeSequence.add(5);
        moduloSequence.add(3);
        moduloSequence.add(5);
        
        /* extract primes from first partition
        * anchor points: prime 5, prime 7
        * starts at 5 ends at 7*7 = 49
        * first two primes corresponding to 5 and 7
        */
        
        // anchor points
        int primeA = 5;
        int primeB = 7;
        
        int startIndex = getSigma(primeA*primeA);        
        int endpoint; // last index of the partition
        int index = startIndex;

        int indexForPartitions = 1;
        int lastElement = 1; 

        while (primeSequence.size() <= N)
        {
            if (indexForPartitions > 1)
            {
            	//index from last element of prior partition
                index = getIndexAtElement(lastElement*primeA); 
            }

            endpoint = getSigma(primeA * primeB * primeB);

            while (index < endpoint - primeA)
            {                    
                int nextPrime;

                index += primeA; // periodicity            

                nextPrime = getMainSequenceElement(index) / primeA;

                boolean isPrime = true; 
                
                // pruning
                for (int module : moduloSequence)
                {
                    if (nextPrime % module == 0)
                    {
                        isPrime = false;
                        break;
                    }
                }

                if (isPrime)
                {
                	primeSequence.add(nextPrime);
                }
            }
            
            // rollover
            moduloSequence.add(primeB);
            primeA = primeB;

            int position = primeSequence.indexOf(primeB);
            primeB = primeSequence.get(position + 1);
            
            indexForPartitions++;

            lastElement = primeSequence.get(primeSequence.size() - 1);
        }

        return primeSequence;         
	}
	
	//print to file list of first N primes
    public void printPrimeSequence(int N)
    {
        ArrayList<Integer> list = getFirstNprimes(N);

        String line = "2";
        int numberColumns = 10;
               
        try
        {
        	File fout = new File("c:\\temp\\zprime.txt");
        	FileOutputStream fos = new FileOutputStream(fout);
            
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < N; i++)
            {                
                if (i > 0)
                {
                    for (int j = 0; j < numberColumns; j++)
                    {
                        if (j > 0)
                        {
                            if(i < list.size())
                            	line += ";" + list.get(i);
                        }
                        else
                        {
                            line += list.get(i);
                        }

                        i++;
                    }                      
                }
                else // first row
                {
                    for (int j = 1; j < numberColumns; j++)
                    {
                        line += ";" + list.get(i);    
                        i++;
                    }    

                }

                bw.write(line);
                bw.newLine();
                line="";
                i--;
            }
            
            bw.close();
        }    
        catch(FileNotFoundException ex1)
        {}
        catch(IOException ex2)
        {}

    }
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		primesGenerator gen = new primesGenerator();
		gen.printPrimeSequence(10000);

	}

}
