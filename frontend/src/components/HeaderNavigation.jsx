import './styles/HeaderNavigation.css'
import { Link } from 'react-router-dom';

const style = {
  link: {
    color: 'white',
    textDecoration: 'none',
    fontWeight: 'bold',
  },
};

export default function HeaderNavigation() {
  return (
    <header>
      <nav>
        <ul>
          <li><Link to="/" style={style.link}>Calculadora</Link></li>
          <li><Link to="/about" style={style.link}>Sobre</Link></li>
        </ul>
      </nav>
    </header>
  );
}