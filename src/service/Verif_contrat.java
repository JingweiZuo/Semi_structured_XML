package service;


public class Verif_contrat {
  public Verif_contrat() {
    // TODO Auto-generated constructor stub
  }

  public boolean verification_contrat(String contrat_donneur){
    if(contrat_donneur.substring(0,1).equals("A")) 
       return true;
    else
       return false;
  }
}