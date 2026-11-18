package sistemagestionempleados.josehernandez;

public class GestorEmpleados {

    private Empleado[] lista = new Empleado[100];
    private int total = 0;

    public boolean agregar(Empleado emp) {
        if (total >= lista.length) {
            return false;
        }
        lista[total] = emp;
        total++;
        return true;
    }
// estructura del for comienza en -1 para no salirnos del arreglo
    public boolean eliminar(int indice) {
        if (indice < 0 || indice >= total) {
            return false;
        }
        for (int i = indice; i < total - 1; i++) {
            lista[i] = lista[i + 1];
        }
        lista[total - 1] = null;
        total--;
        return true;
    }
}