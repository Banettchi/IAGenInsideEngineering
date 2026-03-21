package eci.edu.dosw.parcial.parcial1cvdsdosw01.util;

public enum PaymentStatus {

    PENDING("Pendiente"),
    PROCESSING("Procesando"),
    COMPLETED("Completado"),
    FAILED("Fallido"),
    CANCELED("Cancelado");
    
    private final String name;


    PaymentStatus(String name) {
        this.name = name;
    } 
   
    
    public String getName() { return name; }

}
