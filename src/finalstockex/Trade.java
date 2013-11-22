/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalstockex;

import java.util.Random;

/**
 *
 * @author Twiz
 */
public class Trade
{
    private String type, traderName, action;
    private int quantity, tradeTTL;
    private double price;
    private boolean deleteMe;
    
    public Trade(String action, String type, String traderName, int quantity, double price)
    {
        Random rand = new Random();
        this.action = action;
        this.type = type;
        this.traderName = traderName;
        this.quantity = quantity;
        this.price = price;
        this.tradeTTL = rand.nextInt(8) + 1;
        this.deleteMe = false;
    }
    
    @Override
    public String toString()
    {
        return String.format("\n%s %s %s %d %f\n", action, type, traderName, quantity, price);
    }

    //<editor-fold defaultstate="collapsed" desc="Get and set stuff">
    public void setDeleteMe(boolean deleteMe)
    {
        this.deleteMe = deleteMe;
    }
    
    public boolean isDeleteMe()
    {
        return deleteMe;
    }
    
    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getTraderName()
    {
        return traderName;
    }
    
    public void setTraderName(String traderName)
    {
        this.traderName = traderName;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    public void decTTL()
    {
        this.tradeTTL--;
    }
    
    public void resetTTL()
    {
        Random rand = new Random();
        this.tradeTTL = rand.nextInt(8) + 1;
    }
    
    public int getTTL()
    {
        return this.tradeTTL;
    }
    //</editor-fold>
}
