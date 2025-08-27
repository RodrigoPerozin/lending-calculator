function formatRegisters(data) {
  return data.map(item => ({
    ...item,
    dateRegister: new Date(item.dateRegister).toLocaleDateString('pt-BR', { timeZone: 'UTC' }),
    lendingValue: item.lendingValue.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    outstandingBalance: item.outstandingBalance.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    consolidatedInstallment: item.consolidatedInstallment.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    totalInstallment: item.totalInstallment.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    amortization: item.amortization.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    outstanding: item.outstanding.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    provision: item.provision.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    acumulated: item.acumulated.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
    paid: item.paid.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
  }));
}

export default function ResultLendingTable(props) {
    const data = formatRegisters(props?.data || []);
  return (
    <table className="table table-striped">
      <thead style={{ position: "sticky", top: 0, zIndex: 1 }}>  
        <tr>
            <th scope="col" colSpan={3} className="text-center border-2">Empréstimo</th>
            <th scope="col" colSpan={2} className="text-center border-2">Parcela</th>
            <th scope="col" colSpan={2} className="text-center border-2">Principal</th>
            <th scope="col" colSpan={3} className="text-center border-2">Juros</th>
        </tr>
        <tr>
            <th scope="row" className="text-center border-2">Data Competência</th>
            <th scope="row" className="text-center border-2">Valor de Empréstimo</th>
            <th scope="row" className="text-center border-2">Saldo Devedor</th>

            <th scope="row" className="text-center border-2">Consolidada</th>
            <th scope="row" className="text-center border-2">Total</th>

            <th scope="row" className="text-center border-2">Amortização</th>
            <th scope="row" className="text-center border-2">Saldo</th>

            <th scope="row" className="text-center border-2">Provisão</th>
            <th scope="row" className="text-center border-2">Acumulado</th>
            <th scope="row" className="text-center border-2">Pago</th>
          </tr>
        </thead>
        <tbody>
          {data && data?.length > 0 ? data?.map((item, index) => (
            <tr key={index}>
              <td className="text-center border-2">{item.dateRegister}</td>
              <td className="text-center border-2">{item.lendingValue}</td>
              <td className="text-center border-2">{item.outstandingBalance}</td>

              <td className="text-center border-2">{item.consolidatedInstallment}</td>
              <td className="text-center border-2">{item.totalInstallment}</td>

              <td className="text-center border-2">{item.amortization}</td>
              <td className="text-center border-2">{item.outstanding}</td>

              <td className="text-center border-2">{item.provision}</td>
              <td className="text-center border-2">{item.acumulated}</td>
              <td className="text-center border-2">{item.paid}</td>
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