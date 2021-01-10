package dao;

import orderBook.Stock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StockDAOTest {

    @Before
    public void setup(){

        StockDAO.loadStocks("/home/ivanjericevich/CoinTossX/data");
    }

    @Test
    public void testGetStock() throws Exception {
        Stock stock = new Stock();
        stock.setStockCode(1);
        stock.setMRS(700);
        stock.setTickSize(1);

        Stock storedValue = StockDAO.getStock(1L);

        assertEquals(stock,storedValue);
    }
}
