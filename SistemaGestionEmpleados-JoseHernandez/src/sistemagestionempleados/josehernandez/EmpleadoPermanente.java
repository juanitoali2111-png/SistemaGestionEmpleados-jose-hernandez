package sistemagestionempleados.josehernandez;

public class EmpleadoPermanente extends Empleado {
//nuevo atributo de sub clase llamamos al construc y guarda la antiguedad ingresada
    private int antiguedad;

    public EmpleadoPermanente(String nombre, String apellido, String cedula, int cantidadHijos, int antiguedad) {
        super(nombre, apellido, cedula, cantidadHijos);
        this.antiguedad = antiguedad;
    }
// sobre escribimos metodo
    @Override
    public double calcularSueldo() {
        return 20000.0 + (getCantidadHijos() * 1000.0) + (antiguedad * 1000.0);
    }

    @Override
    public String getTipoEmpleado() {
        return "Empleado Planta Permanente";
    }
// texo de antiguedad en tabla  
    @Override
    public String getDetalle() {
        return "Antigüedad: " + antiguedad + " años";
    }
}