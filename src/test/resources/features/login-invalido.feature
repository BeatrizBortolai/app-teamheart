# language: pt

Funcionalidade: Validacao de login

  Como usuario da plataforma ESG
  Quero validar minhas credenciais
  Para impedir acessos invalidos

  Cenario: Login invalido
    Dado que possuo credenciais invalidas
    Quando envio uma requisicao POST para "/auth/login"
    Entao o sistema deve retornar status code 400
    E o corpo da resposta deve conter mensagem de erro




