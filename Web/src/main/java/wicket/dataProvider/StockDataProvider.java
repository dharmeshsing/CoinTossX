package wicket.dataProvider;

import com.carrotsearch.hppc.ObjectArrayList;
import dao.StockDAO;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import vo.StockVO;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by dharmeshsing on 29/04/16.
 */
public class StockDataProvider extends SortableDataProvider<StockVO, String> {

    private transient ObjectArrayList<StockVO> stockVOList = new ObjectArrayList<>(100);
    private String stockFile;

    public StockDataProvider(String dataPath) throws Exception {
        stockFile = dataPath + File.separator + "Stock.csv";
        StockDAO.loadStocks(stockFile, stockVOList);
    }


    @Override
    public Iterator<? extends StockVO> iterator(long first, long count) {
        return Arrays.stream(stockVOList.buffer).map(e -> (StockVO)e).filter(trade -> trade != null).collect(Collectors.toList()).iterator();
    }

    @Override
    public long size() {
        return stockVOList.size();
    }

    @Override
    public IModel<StockVO> model(StockVO object) {
        return new LoadableDetachableModel(object){

            @Override
            protected Object load() {
                int index = stockVOList.indexOf(object);
                if(index != -1){
                    return stockVOList.get(index);
                }else{
                    return null;
                }
            }
        };
    }

    public StockVO getStock(int securityId){
        for (int i=0, max=stockVOList.size(); i < max; i++) {
            StockVO stockVO = stockVOList.get(i);
            if(stockVO != null && stockVO.getSecurityId() == securityId){
                return stockVO;
            }
        }

        return null;
    }

    public void addStock(StockVO stockVO) throws Exception {
        stockVOList.add(stockVO);
        saveStocks();
    }

    private void saveStocks() throws Exception {
        StockDAO.writeCsvFile(stockFile, stockVOList);
    }

    public ObjectArrayList<StockVO> getStocks(){
        return stockVOList;
    }


}
