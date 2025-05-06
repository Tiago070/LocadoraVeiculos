package testes;


public class Pagamento {
    
    public class Pagamento {
    private int id;
    private Aluguel aluguel;
    private String formaPagamento;
    private double valor;

    public Pagamento(int id, Aluguel aluguel, String formaPagamento) {
        this.id = id;
        this.aluguel = aluguel;
        this.formaPagamento = formaPagamento;
        this.valor = aluguel.calcularTotal();
    }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Aluguel getAluguel() {
            return aluguel;
        }

        public void setAluguel(Aluguel aluguel) {
            this.aluguel = aluguel;
        }

        public String getFormaPagamento() {
            return formaPagamento;
        }

        public void setFormaPagamento(String formaPagamento) {
            this.formaPagamento = formaPagamento;
        }

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }

    
}
    
}
