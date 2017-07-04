package wicket.dataProvider;

import com.carrotsearch.hppc.ObjectArrayList;
import dao.ClientDAO;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import vo.ClientVO;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by dharmeshsing on 29/04/16.
 */
public class ClientDataProvider extends SortableDataProvider<ClientVO, String> {


    private transient ObjectArrayList<ClientVO> clientVOList = new ObjectArrayList<>();
    private String clientFile;

    public ClientDataProvider(String dataPath) throws Exception {
        clientFile = dataPath + File.separator + "clientData.csv";
        ClientDAO.loadClients(clientFile, clientVOList);
    }


    @Override
    public Iterator<? extends ClientVO> iterator(long first, long count) {
        return Arrays.stream(clientVOList.buffer).map(e -> (ClientVO)e).filter(trade -> trade != null).collect(Collectors.toList()).iterator();
    }

    @Override
    public long size() {
        return clientVOList.size();
    }

    @Override
    public IModel<ClientVO> model(ClientVO object) {
        return new LoadableDetachableModel(object){

            @Override
            protected Object load() {
                int index = clientVOList.indexOf(object);
                if(index != -1){
                    return clientVOList.get(index);
                }else{
                    return null;
                }
            }
        };
    }

    public void addClient(ClientVO clientVO) throws Exception {
        clientVOList.add(clientVO);
        saveClients();
    }

    private void saveClients() throws Exception {
        ClientDAO.writeCsvFile(clientFile, clientVOList);
    }

    public ObjectArrayList<ClientVO> getClients(){
        return clientVOList;
    }

    public ClientVO getClient(int id){
        for (int i=0, max=clientVOList.size(); i < max; i++) {
            ClientVO clientVO = clientVOList.get(i);
            if(clientVO.getCompId() == id){
                return clientVO;
            }
        }

        return null;
    }

    public void removeClient(ClientVO clienVO){
        clientVOList.removeFirst(clienVO);
    }
}
