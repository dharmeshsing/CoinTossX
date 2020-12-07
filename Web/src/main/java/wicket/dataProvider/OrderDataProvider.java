package wicket.dataProvider;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import vo.OrderVO;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by dharmeshsing on 16/04/16.
 */
public class OrderDataProvider extends SortableDataProvider<OrderVO, String> {

    private Collection<OrderVO> orders;

    public OrderDataProvider(Collection<OrderVO> orders){
        this.orders = orders;
    }

    public OrderDataProvider(){
    }

    @Override
    public Iterator<? extends OrderVO> iterator(long first, long count) {
        return orders.iterator();
    }

    @Override
    public long size() {
        return orders == null ? 0:orders.size();
    }

    @Override
    public IModel<OrderVO> model(OrderVO object) {
        return new LoadableDetachableModel(object){

            @Override
            protected Object load() {
                Optional<OrderVO> opt = orders.stream().filter(o-> o.equals(object)).findFirst();
                if(opt.isPresent()){
                    return opt.get();
                }else{
                    return null;
                }
            }
        };
    }

    public void clear(){
        orders.clear();
    }

    public void addOrderVO(OrderVO orderVO){
        orders.add(orderVO);
    }

    public Collection<OrderVO> getOrderVOs(){
        return orders;
    }

}
