package sistemagestionempleados.josehernandez;

public class EmpleadoPorHora extends Empleado {
//atributo extra de la sub clase, con super llama los daos basicos y pasa cant hijos a 0
    private int horasTrabajadasMes;

    public EmpleadoPorHora(String nombre, String apellido, String cedula, int horasTrabajadasMes) {
        super(nombre, apellido, cedula, 0);
        //guarda cantidad de horas trabajadas
        this.horasTrabajadasMes = horasTrabajadasMes;
    }

    public int getHorasTrabajadasMes() {
        return horasTrabajadasMes;
    }
//sobreescribimos metodos 
    @Override
    public double calcularSueldo() {
        return horasTrabajadasMes * 100.0;
    }

    @Override
    public String getTipoEmpleado() {
        return "Empleado por Hora";
    }

    @Override
    public String getDetalle() {
        return "Horas trabajadas en el mes: " + horasTrabajadasMes;
    }
//no  tiene hijos
    @Override
    public String getTextoHijos() {
        return "No aplica";
    }
}