/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;


import entities.ArticuloDonar;
import entities.Categoria;
import entities.Pertenece;
import entities.Usuario;
import facades.ArticuloDonarFacade;
import facades.CategoriaFacade;
import facades.PerteneceFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import tools.FacesPrinter;
/**
 *
 * @author Arturo
 */
@Named("addDonationsManager")
@SessionScoped
public class AddDonationsManager implements Serializable {
    @EJB
    private ArticuloDonarFacade ArticuloFacade;
    @EJB
    private PerteneceFacade perteneceFacade;
    @EJB
    private CategoriaFacade categoriaFacade;
    @Inject LoginManager loginManager;
    
    private ArticuloDonar Articulo;
    private List<String> categorias;
     /**
     * costructor de la clase, inicializa el objeto tipo articuloDonar
     */ 
    public AddDonationsManager(){
       Articulo = new ArticuloDonar();
    }
     /**
     * retorna un objeto 
     * @return objeto tipo articuloDonar
     */
    public ArticuloDonar getArticulo() {
        return Articulo;
    }
    /**
     * Asigna el contenido al objeto de la clase
     * @param Articulo objeto tipo articuloDonar
     */
    public void setArticulo(ArticuloDonar Articulo) {
        this.Articulo = Articulo;
    }
    /**
     * comprueba si la variable esta bacia, de estarlo retorna un false
     * si esta llena retorna la cadena sin espacios al inicio o final
     * @param str una cadena
     * @return la cadena
     */
    private boolean isEmpty(String str){
        if(str==null)return false;
        return str.trim().equals("");
    }
    /**
     * pubica un articulo, obtiene la sinopsis, tema, descripcion e imagen, en 
     * case de que alguna propiedad falte se lanza un mensaje de advertencia.
     * Si esta todo se obtiene elusuario, se crea el objeto tipo articulo
     * @return retorna una aviso de "list_donations"
     */
    public String publicarArticulo()
    {
        boolean invalid = false;
        invalid |= isEmpty(Articulo.getSinopsis());
        invalid |= isEmpty(Articulo.getTema());
        invalid |= isEmpty(Articulo.getDescripcion());
        invalid |= isEmpty(Articulo.getImagen());
        
         if (invalid){
            FacesPrinter.addMsg("Por favor rellene todos los campos");
            return null;
        } 

        
         Articulo.setUsuario(loginManager.getCurrentUser());
         ArticuloFacade.create(Articulo);
         for(String cat:categorias){
            Pertenece pertenece = new Pertenece();
            pertenece.setArticulo(Articulo);
            Categoria categoria = new Categoria();
            categoria.setId(Integer.parseInt(cat));
            pertenece.setCategoria(categoria);
            perteneceFacade.create(pertenece);
         }
         Articulo = new ArticuloDonar();
         return "list_donations";
    }
    /**
     * recibe una lista con las categorias para agregarlas
     */
    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }
     /**
     * asigna una lista de categorias para agregarlas
     */
    public List<String> getCategorias() {
        return categorias;
    }

    
        
}
