package sistemagestionempleados.josehernandez;

public class EmpleadoTemporal extends Empleado {
//atributos nuevos para sub clase
    private String fechaIngreso;
    private String fechaFinContrato;
//llama al constructor trae datos basicos y guarda los datos de los atribuutos nuevos
    public EmpleadoTemporal(String nombre, String apellido, String cedula, int cantidadHijos,
                            String fechaIngreso, String fechaFinContrato) {
        super(nombre, apellido, cedula, cantidadHijos);
        this.fechaIngreso = fechaIngreso;
        this.fechaFinContrato = fechaFinContrato;
    }
//regresa datos 
    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public String getFechaFinContrato() {
        return fechaFinContrato;
    }
// sobreescribimos metodos hijos +1000
    @Override
    public double calcularSueldo() {
        return 18000.0 + (getCantidadHijos() * 1000.0);
    }

    @Override
    public String getTipoEmpleado() {
        return "Empleado Temporal";
    }
//ingreso fecha tal fin fecha x
    @Override
    public String getDetalle() {
        return "Ingreso: " + fechaIngreso + " | Fin: " + fechaFinContrato;
    }
}