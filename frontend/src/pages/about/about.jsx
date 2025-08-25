import './about.css'

export default function About() {
  return (
    <div className="about-page">
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-md-8">
            <div className="card shadow-sm">
              <div className="card-body">
                <h1 className="card-title mb-4 text-center">Sobre a Calculadora de Empréstimos</h1>
                <p>
                  O <strong>Calculadora de Empréstimos</strong> é um site desenvolvido para ajudar você a calcular empréstimos de forma simples e rápida.
                  Com ele, é possível simular valores, taxas de juros e períodos de pagamento, facilitando o planejamento financeiro.
                </p>
                <p>
                  Basta informar o valor desejado, a taxa de juros e o prazo para obter uma estimativa detalhada das parcelas e do custo total do empréstimo.
                </p>
                <p>
                  Este projeto foi criado por <strong>Rodrigo Destri Perozin</strong>, com o objetivo de auxiliar no processo de decisão sobre empréstimos, em função do processo seletivo da <a href='https://www.totvs.com/'>TOTVS</a>.
                </p>
                <p className="text-muted text-end mb-0">
                  &copy; {new Date().getFullYear()} Rodrigo Destri Perozin
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}