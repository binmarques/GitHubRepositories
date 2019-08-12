# GitHubRepositories
Projeto desafio para consumir a API do GitHub e exibir os repositórios mais populares de Java.
O projeto faz uso do padrão de arquitetura MVP, das bibliotecas Retrofit, RxJava para programação reativa, features do Java 8 como expressões lambda,
paginação e scroll infinito, cache de dados local utilizando Room para funcionamento offline do app e mais...

# O app objetiva-se

* Consultar a API do GitHub e exibir os repositórios mais populares de Java

* Consultar a API do GitHub e exibir os "pull requests" pertencentes ao repositório previamente selecionado na lista de repositórios

* Ao clicar em algum pull request o app direciona o usuário a página do GitHub com todas as informações correspondentes ao pull request selecionado

# Telas

![ScreenShot](https://raw.github.com/binmarques/GitHubRepositories/master/art/repositories.png)
![ScreenShot](https://raw.github.com/binmarques/GitHubRepositories/master/art/pull_requests.png)
![Repositories Gif](art/my_gif.gif)

# Bibliotecas utilizadas 

* AppCompat - biblioteca de suporte a versões mais antigas do Android

* MaterialComponents - biblioteca de design da google para componentes do material design 

* CardView - biblioteca para exibição de cards

* RecyclerView - biblioteca para uso do recyclerview para as listas presentes no app

* ConstraintLayout - biblioteca para uso de layouts mais ajustáveis e de melhor desempenho

* ButterKnife - biblioteca responsável pelo bind das views com a classe, diminuindo o boilerplate code

* Retrofit - biblioteca cliente para as chamadas HTTP no app

* RXJava - biblioteca de programação reativa no app

* Glide - biblioteca de manipulação e armazenamento em cash das imagens presentes no app

# Referências

* AppCompat - https://github.com/codepath/android_guides/wiki/Migrating-to-the-AppCompat-Library

* MaterialComponents - https://github.com/material-components

* ButterKnife - http://jakewharton.github.io/butterknife/

* ButterKnife - https://github.com/JakeWharton/butterknife

* Retrofit - https://github.com/square/retrofit

* RXJava - https://github.com/ReactiveX/RxJava

* Glide - https://github.com/bumptech/glide
