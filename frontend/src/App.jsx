import { Route, Routes } from 'react-router'
import './App.css'
import HeaderNavigation from './components/HeaderNavigation.jsx'
import Calculator from './pages/calculator/Calculator.jsx'
import About from './pages/about/about.jsx'
import ErrorPage from './pages/error/ErrorPage.jsx'

function App() {

  return (
    <div className="App">
      <HeaderNavigation/>
      <Routes>
        <Route static exact path="/" element={<Calculator/>} errorElement={<ErrorPage title='Página não encontrada' code={404}/>}/>
        <Route static exact path="/about" element={<About/>} errorElement={<ErrorPage title='Página não encontrada' code={404}/>}/>
        <Route path="*" element={<ErrorPage title='Página não encontrada' code={404}/>}/>
      </Routes>
    </div>
  )
}

export default App
