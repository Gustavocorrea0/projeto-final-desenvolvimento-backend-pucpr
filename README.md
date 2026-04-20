# Desenvolvimento Backend

Código final da aula 02 - Persistência

Nesta aula, adicionamos um serviço para inserção de papéis e formas de vincular o usuário a seu papel.
Não contém a resolução do exercício (proibir a exclusão do último administrador)

## Arquivos modificados
- build.gradle.kts: Adicionada a biblioteca do JPA e configurado o plugin
- application.yaml: Configurações do JPA

## Classes modificadas

- User: Mapeamento do usuário no JPA, lista de papéis
- UserRepository: Alterado para usar o JPA
- UserService: Inserção com validação do email único
- UserController

## Classes adicionadas

- Role: Papel possível para o usuário
- RoleRepository, RoleService e RoleController: Classes MVC do papel, similar as do usuário
- Boostrapper: Insere os papéis USER e PREMIUM e o usuário do administrador
- Note que a camada de serviço garante que o nome do role seja sempre maiúsculo