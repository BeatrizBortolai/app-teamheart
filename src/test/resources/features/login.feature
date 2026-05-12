# language: pt

Funcionalidade: Login de usuarios

  Como usuario da plataforma ESG
  Quero realizar login no sistema
  Para acessar as funcionalidades da aplicacao

  Cenario: Login com sucesso
    Dado que possuo credenciais validas
    Quando envio uma requisicao POST para "/auth/login"
    Entao o sistema deve retornar status code 200
    E o corpo da resposta deve conter a mensagem de login realizado
