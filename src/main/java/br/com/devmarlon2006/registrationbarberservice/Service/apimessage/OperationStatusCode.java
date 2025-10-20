package br.com.devmarlon2006.registrationbarberservice.Service.apimessage;


public enum OperationStatusCode {
    // ----------------------------------------------------
    // Códigos de Sucesso (Para log e rastreamento de operações bem-sucedidas)
    // ----------------------------------------------------
    SUCCESS_ENTITY_CREATED(100, "Entidade criada e persistida com sucesso."),
    SUCCESS_ENTITY_UPDATED(101, "Entidade atualizada com sucesso na base de dados."),
    SUCCESS_ENTITY_DELETED(102, "Entidade removida com sucesso."),
    SUCCESS_DATA_FOUND(103, "Dados encontrados e retornados com sucesso."),
    SUCCESS_ASSOCIATION_MADE(104, "Associação entre entidades realizada com sucesso."),
    SUCESS_EXPECTED(105 , "Operação realizada com sucesso."),

    // ----------------------------------------------------
    // Códigos de Erro de Persistência (Relacionados ao Banco de Dados/JPA)
    // ----------------------------------------------------
    ERROR_DB_SAVE_FAILED(200, "Erro ao salvar a entidade no banco de dados."),
    ERROR_DB_UPDATE_FAILED(201, "Erro ao atualizar a entidade no banco de dados."),
    ERROR_DB_DELETE_FAILED(202, "Erro ao tentar remover a entidade do banco de dados."),
    ERROR_DB_CONNECTION(203, "Falha na conexão ou indisponibilidade do banco de dados."),
    ERROR_UNIQUE_CONSTRAINT(204, "Violação de restrição de unicidade no banco de dados."),
    ERROR_FOREIGN_KEY_CONSTRAINT(205, "Violação de chave estrangeira: Entidade associada não existe ou está em uso."),

    // ----------------------------------------------------
    // Códigos de Erro de Processamento/Negócio (Validação e Lógica)
    // ----------------------------------------------------
    ERROR_PROCESSING_ENTITY(300, "Erro genérico durante o processamento da entidade."),
    ERROR_INVALID_ID_FORMAT(301, "O formato do ID fornecido é inválido."),
    ERROR_ID_ASSOCIATION_FAILED(302, "Erro ao tentar associar um ID a uma entidade inexistente."),
    ERROR_ENTITY_NOT_FOUND(303, "Entidade não encontrada para a operação solicitada."),
    ERROR_VALIDATION_FAILED(304, "Falha na validação de um ou mais campos da entidade."),
    ERROR_BUSINESS_RULE(305, "Regra de negócio não atendida para a operação."),

    // ----------------------------------------------------
    // Códigos de Erro Inesperado/Geral
    // ----------------------------------------------------
    ERROR_UNEXPECTED(500, "Ocorreu um erro interno e inesperado na API.");

    private final int code;
    private final String message;

    OperationStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Retorna a mensagem formatada com um detalhe adicional.
     * @param detail O detalhe específico a ser anexado à mensagem.
     * @return String com a mensagem e o detalhe.
     */
    public String getFormattedMessage(String detail) {
        return String.format("[%d] %s Detalhe: %s", this.code, this.message, detail);
    }
}
