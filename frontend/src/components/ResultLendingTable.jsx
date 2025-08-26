export default function ResultLendingTable(props) {
    const data = props?.data || [];

  return (
    <table className="table table-striped">
      <thead>  
        <tr>
            <th scope="col" colSpan={3} className="text-center border-2">Empréstimo</th>
            <th scope="col" colSpan={2} className="text-center border-2">Parcela</th>
            <th scope="col" colSpan={2} className="text-center border-2">Principal</th>
            <th scope="col" colSpan={3} className="text-center border-2">Juros</th>
        </tr>
        </thead>
        <tbody>
          <tr>
            <td scope="row" className="text-center border-2 th">Data Competência</td>
            <td scope="row" className="text-center border-2 th">Valor de Empréstimo</td>
            <td scope="row" className="text-center border-2 th">Saldo Devedor</td>

            <td scope="row" className="text-center border-2 th">Consolidada</td>
            <td scope="row" className="text-center border-2 th">Total</td>

            <td scope="row" className="text-center border-2 th">Amortização</td>
            <td scope="row" className="text-center border-2 th">Saldo</td>

            <td scope="row" className="text-center border-2 th">Provisão</td>
            <td scope="row" className="text-center border-2 th">Acumulado</td>
            <td scope="row" className="text-center border-2 th">Pago</td>
          </tr>
          {data && data?.length > 0 ? data?.map((item, index) => (
            <tr key={index}>
              <td className="text-center border-2">{item.competenceDate}</td>
              <td className="text-center border-2">{item.lendingValue}</td>
              <td className="text-center border-2">{item.debtBalance}</td>

              <td className="text-center border-2">{item.installmentConsolidated}</td>
              <td className="text-center border-2">{item.installmentTotal}</td>

              <td className="text-center border-2">{item.amortization}</td>
              <td className="text-center border-2">{item.amortizationBalance}</td>

              <td className="text-center border-2">{item.provision}</td>
              <td className="text-center border-2">{item.provisionAccumulated}</td>
              <td className="text-center border-2">{item.provisionPaid}</td>
            </tr>
          )) : (
            <tr>
              <td colSpan={10} className="text-center border-2">Nenhum dado disponível</td>
            </tr>
          )}
        </tbody>
    </table>
  )
}