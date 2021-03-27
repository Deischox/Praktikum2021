package basecamp.project.server.controller;

import java.io.Serializable;

/**
* The Class word is used to have an easy to access data type for the frontend to work with
*
* @author  Silas Ueberschaer
* @version 2.0
* @since   2021-03-27
*/
public class word implements Serializable{
    public String wo;
    public Float amount;

    /**
     * Initilaize the class word with the value for wo (The word) and amount (The amount)
     * 
     * @param w The word 
     * @param a The amount 
     */
    public word(String w, Float a)
    {
        wo = w;
        amount = a;
    }

    /**
     * This method is used to get the amount of the word
     * 
     * @return The amount 
     */
    public float getAmount()
    {
        return amount;
    }
}
