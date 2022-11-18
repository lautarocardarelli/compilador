package lyc.compiler.model;

public class Simbolo {
    public String nombre;
    public String tipo;
    public String valor;
    public int longitud;

    public Simbolo(){
    }

    public Simbolo(String nombre, String tipo, String valor, int longitud) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.longitud = longitud;
    }

    public Simbolo(String nombre, String tipo, String valor) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Simbolo(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public int getLongitud() {
        return longitud;
    }

    public void mostrar_vector(){
        System.out.println("Nombre: " + nombre +"Tipo: " + tipo +"Valor: " + valor + "Longitud: " + longitud+ "\n");
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Simbolo))
            return false;
        Simbolo other = (Simbolo)o;

        if ( this.nombre.equals(other.getNombre()) && this.tipo.equals(other.getTipo()) ) return true;
        return false;
    }

    @Override
    public final int hashCode() {
        return this.nombre.hashCode() + this.tipo.hashCode();
    }
}