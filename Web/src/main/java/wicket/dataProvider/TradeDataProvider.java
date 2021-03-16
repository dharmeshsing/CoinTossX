package wicket.dataProvider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import vo.TradeVO;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class TradeDataProvider extends SortableDataProvider<TradeVO, String> {

    private Collection<TradeVO> trades;

    public TradeDataProvider(Collection<TradeVO> trades){
        this.trades = trades;
    }

    @Override
    public Iterator<? extends TradeVO> iterator(long first, long count) {
        return trades.iterator();
    }

    @Override
    public long size() {
        return trades ==null ? 0 : trades.size();
    }

    @Override
    public IModel<TradeVO> model(TradeVO object) {
        return new LoadableDetachableModel(object){

            @Override
            protected Object load() {
                Optional<TradeVO> opt = trades.stream().filter(o-> o.equals(object)).findFirst();
                if(opt.isPresent()){
                    return opt.get();
                }else{
                    return null;
                }
            }
        };
    }

    @Override
    public void detach() {

    }
}
