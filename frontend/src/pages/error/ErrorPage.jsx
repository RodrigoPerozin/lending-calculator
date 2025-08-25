import React from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTriangleExclamation } from "@fortawesome/free-solid-svg-icons";

export default function ErrorPage({
    code = 500,
    title = "Something went wrong",
    message = "Um erro inesperado ocorreu. Por favor, tente novamente mais tarde.",
    onRetry,
    icon = faTriangleExclamation,
    iconColor = "white"
}) {
    return (
        <div className="d-flex align-items-center justify-content-center vh-100 bg-light">
            <div className="card shadow-lg border-0" style={{ maxWidth: "32rem" }}>
                <div className="card-body text-center p-5">
                    <div className="mb-4">
                        <div className="bg-warning text-white rounded-circle d-inline-flex align-items-center justify-content-center" style={{ width: "64px", height: "64px" }}>
                            <i className="bi bi-exclamation-triangle fs-2">
                                <FontAwesomeIcon color={iconColor} icon={icon} />
                            </i>
                        </div>
                    </div>
                    <h1 className="display-4 fw-bold text-dark">{code}</h1>
                    <h2 className="h4 fw-semibold text-secondary mb-3">{title}</h2>
                    <p className="text-muted mb-4">{message}</p>
                    <div className="d-flex flex-wrap justify-content-center gap-2">
                        <button
                            onClick={() => window.history.back()}
                            className="btn btn-outline-secondary"
                        >
                            Voltar
                        </button>
                        <a href="/" className="btn btn-outline-primary">
                            Página Inicial
                        </a>
                        {onRetry && (
                            <button onClick={onRetry} className="btn btn-outline-danger">
                                Tentar novamente
                            </button>
                        )}
                    </div>
                    <div className="mt-4 text-muted small">
                        <i className="bi bi-dot"></i> Error page • Powered by React
                    </div>
                </div>
            </div>
        </div>
    );
}