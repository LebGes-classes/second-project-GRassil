package services.location.storage.psm;

import dao.storage.PostamatDAO;
import models.location.storage.psm.Postamat;

import java.util.List;


public class PostamatService {

    public static List<Postamat> getAll(){
        PostamatDAO postamatDAO = new PostamatDAO();
        return postamatDAO.getAll();
    }

    public static Postamat get(int id){
        PostamatDAO postamatDAO = new PostamatDAO();
        return postamatDAO.getById(id);
    }


}
