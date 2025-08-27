import { useState } from 'react';
import './calculator.css'
import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'
import { NumericFormat } from "react-number-format";
import 'react-tooltip/dist/react-tooltip.css';
import { Tooltip } from 'react-tooltip'
import $ from 'jquery';
import ResultLendingTable from '../../components/ResultLendingTable';

const swal = withReactContent(Swal);


export default function Calculator() {
  
  const now = new Date();
  const yyyy = now.getFullYear();
  const mm = String(now.getMonth() + 1).padStart(2, '0');
  const dd = String(now.getDate()).padStart(2, '0');

  const [data, setData] = useState([]);

  const [params, setParams] = useState({
    initialDate: `${yyyy}-${mm}-${dd}`,
    finalDate: '',
    firstPay: '',
    lendingValue: "R$ 140.000,00",
    rawLendingValue: 140000,
    tax: '0%',
    rawTax: 0.0
  });

  const handleInitialDateChange = (e) => {
    let initialDate = new Date(e.target.value);
    let finalDate = new Date(params.finalDate);
    if(params.finalDate !== '' && initialDate >= finalDate) {
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "A data inicial deve ser menor que a data final!",
        showConfirmButton: false,
        timer: 3000,
      });
      return false;
    }
    if(e.target.value == ''){
      setParams({...params, initialDate: e.target.value, firstPay: ''});
    }else{
      setParams({...params, initialDate: e.target.value});
    }
  }

  const handleFinalDateChange = (e) => {
    let finalDate = new Date(e.target.value);
    let initialDate = new Date(params.initialDate);
    if(params.initialDate !== '' && finalDate <= initialDate) {
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "A data final deve ser maior que a data inicial!",
        showConfirmButton: false,
        timer: 3000,
      });
      return false;
    }
    if(e.target.value == ''){
      setParams({...params, finalDate: e.target.value, firstPay: ''});
    }else{
      setParams({...params, finalDate: e.target.value});
    }
  }

  const shouldFirstPayBeDisabled = params.initialDate === '' || params.finalDate === '';

  const handleFirstPayChange = (e) => {
    if(params.initialDate === '' || params.finalDate === ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Defina a data inicial e final primeiro!",
        showConfirmButton: false,
        timer: 3000,
      });
      e.preventDefault()
      return;
    }
    let firstPayDate = new Date(e.target.value);
    let initialDate = new Date(params.initialDate);
    let finalDate = new Date(params.finalDate);
    if(firstPayDate <= initialDate || firstPayDate >= finalDate) {
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "O primeiro pagamento deve estar entre a data inicial e final!",
        showConfirmButton: false,
        timer: 3000,
      });
      return false;
    }else{
      setParams({...params, firstPay: e.target.value});
    }
  }

  const handleLendingValueChange = (e) => {
    let rawValue = e.target.value.replace(/\D/g, "");
    let numericValue = Number(rawValue) / 100;
    if(numericValue > 999999999999.99){
      let formatted = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }).format(1000000000000.00);
      setParams({...params, lendingValue: formatted, rawLendingValue: 1000000000000.00});
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Os empréstimos tem um limite de até 1 trilhão!",
        showConfirmButton: false,
        timer: 3000,
      });
    }else if(numericValue < 0.5){
      let formatted = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }).format(numericValue);
      setParams({...params, lendingValue: formatted, rawLendingValue: numericValue});
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "O empréstimo precisa ser de no mínimo R% 0,50!",
        showConfirmButton: false,
        timer: 3000,
      });
    }else{
      let formatted = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }).format(numericValue);
      setParams({...params, lendingValue: formatted, rawLendingValue: numericValue});
    }
  }

  const handleTaxChange = (values) => {
    if (values.value == "") {
      setParams({...params, tax: '0%', rawTax: 0.0});
    } else {
      setParams({...params, tax: values.formattedValue, rawTax: values.floatValue});
    }
  }

  const handleCalculateLending = () => {
    if(params.initialDate == ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Preencha a data inicial!",
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }else if(params.finalDate == ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Preencha a data final!",
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }else if(params.firstPay == ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Preencha a data do primeiro pagamento!",
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }else if(parseFloat(params.lendingValue.replace(/[^\d,.-]/g, "").replace(",", ".")) < 0.5 || params.lendingValue == ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Preencha o valor do empréstimo corretamente!",
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }else if(params.tax == ''){
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "warning",
        title: "Preencha a taxa de juros!",
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    $.ajax({
      url: 'http://localhost:5172/api/calculator/calculate',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(params),
      dataType: 'json',
    }).done(function(response) {
      setData(response);
      if(response.length == 0){
        swal.fire({
          toast: true,
          position: "top-end",
          icon: "info",
          title: "Nenhum resultado encontrado para os parâmetros informados.",
          showConfirmButton: false,
          timer: 3000,
        });
      }
    }
    ).fail(function() {
      swal.fire({
        toast: true,
        position: "top-end",
        icon: "error",
        title: "Erro ao conectar com o servidor. Tente novamente mais tarde.",
        showConfirmButton: false,
        timer: 3000,
      });
    });
    
  }

  return (
    <div className="calculator-page" id="calculator-page">
      <h1>Calculadora de Empréstimos</h1>
      <div className="filter-container row">
        <div className="col"> 
          <label className="form-label">Data Inicial</label>
          <input type="date" value={params.initialDate} onChange={handleInitialDateChange} className="form-control" />
        </div>
        <div className="col"> 
          <label className="form-label">Data Final</label>
          <input type="date" onChange={handleFinalDateChange} value={params.finalDate} className="form-control" />
        </div>
        <div className="col"> 
          <label className="form-label">Primeiro Pagamento</label>
          <input type="date" data-tooltip-id="tooltip-universal" data-tooltip-content="Preencha a data inicial e final antes!" onChange={handleFirstPayChange} disabled={shouldFirstPayBeDisabled} value={params.firstPay} className="form-control" />
        </div>
        <div className="col">
          <label className="form-label">Valor do empréstimo</label>
          <input type="text" value={params.lendingValue} onChange={handleLendingValueChange} className="form-control" />
        </div>
        <div className="col">
          <label className="form-label">Taxa de juros</label>
          <NumericFormat suffix="%" decimalSeparator="," decimalScale={2} allowNegative={false} isAllowed={(values) => {
            const { floatValue } = values;
            return (floatValue >= 0 && floatValue <= 100);
          }} 
          onValueChange={handleTaxChange} value={params.tax} className="form-control" />
        </div>
        <div className="calculate-btn-div col">
          <input type="button" className="form-control btn-primary calculate-btn" onClick={handleCalculateLending} value="Calcular"/>
        </div>
      </div>
      <Tooltip place="bottom" hidden={!shouldFirstPayBeDisabled} id="tooltip-universal" />
      <div className='results-container'>
          <ResultLendingTable data={data}/>
      </div>
    </div>
  )
}