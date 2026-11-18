package sistemagestionempleados.josehernandez;

public abstract class Empleado {
//define datos de empleado en privado para no poduena modificarse
    private String nombre;
    private String apellido;
    private String cedula;
    private int cantidadHijos;
//guarda datos de empleado basicos
    public Empleado(String nombre, String apellido, String cedula, int cantidadHijos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.cantidadHijos = cantidadHijos;
    }
//regresa los valores ingresados
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public int getCantidadHijos() {
        return cantidadHijos;
    }
// diseño de metodos para sobreescribirse en las sub clases
    public abstract double calcularSueldo();

    public abstract String getTipoEmpleado();

    public abstract String getDetalle();

    public String getTextoHijos() {
        return String.valueOf(cantidadHijos);
    }
}