# language: pt

Funcionalidade: Busca de candidatos

  Como recrutador da plataforma
  Quero buscar candidatos cadastrados
  Para localizar informacoes no sistema

  Cenario: Busca de candidato inexistente
    Dado que nao existe candidato cadastrado com o ID informado
    Quando envio uma requisicao GET para "/candidatos/9999"
    Entao o sistema deve retornar status code 404
    E o corpo da resposta deve conter mensagem de candidato nao encontrado
