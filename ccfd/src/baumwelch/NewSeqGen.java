/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baumwelch;

public class NewSeqGen
{

	public static int[] seqGen( int o[], int item)
	{
		int modO[] = new int[o.length];
		int i;

		for( i  = 0; i<o.length-1; i++)
			modO[i] = o[i+1];
		modO[i] = item;

		return modO;
   }
}


