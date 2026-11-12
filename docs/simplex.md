# Guia Teórico: Programação Linear e o Método Simplex

Este documento explica, sem exigir conhecimento prévio, o que é Programação Linear, como o método Simplex funciona e como cada conceito aparece no código do projeto. Ele é complementar ao Javadoc: aqui está a visão geral; nas classes, os detalhes de implementação.

## Sumário

1. [O que é Programação Linear](#1-o-que-é-programação-linear)
2. [A intuição geométrica](#2-a-intuição-geométrica)
3. [Por que o Simplex](#3-por-que-o-simplex)
4. [Da desigualdade à igualdade: variáveis de folga, excesso e artificiais](#4-da-desigualdade-à-igualdade-variáveis-de-folga-excesso-e-artificiais)
5. [O Tableau](#5-o-tableau)
6. [Uma iteração: pivô de coluna, pivô de linha e pivoteamento](#6-uma-iteração-pivô-de-coluna-pivô-de-linha-e-pivoteamento)
7. [Exemplo completo resolvido](#7-exemplo-completo-resolvido)
8. [O método Big-M](#8-o-método-big-m)
9. [Minimização](#9-minimização)
10. [Interpretação dos resultados](#10-interpretação-dos-resultados)
11. [Limitações conhecidas](#11-limitações-conhecidas)
12. [Glossário](#12-glossário)
13. [Mapa entre teoria e código](#13-mapa-entre-teoria-e-código)

---

## 1. O que é Programação Linear

Em muitos problemas reais deseja-se **maximizar** algo benéfico (lucro, vazão de requisições) ou **minimizar** algo custoso (gastos, tempo ocioso, uso de memória), respeitando **recursos limitados**: orçamento, servidores disponíveis, horas de trabalho, matéria-prima.

A **Programação Linear (PL)** é a técnica matemática que traduz esse cenário em um modelo com três elementos:

| Elemento | Descrição | Exemplo |
|---|---|---|
| **Variáveis de decisão** ($x_1, x_2, \dots$) | O que se deseja descobrir. | Quantas unidades de cada produto fabricar. |
| **Função objetivo** ($Z$) | A quantidade a otimizar, escrita como soma ponderada das variáveis. | $Z = 3x_1 + 5x_2$ (lucro total) |
| **Restrições** | Limites que a solução deve respeitar, escritos como $\le$, $\ge$ ou $=$. | $x_1 \le 4$, $3x_1 + 2x_2 \le 18$ |

O adjetivo **linear** significa que todas as expressões são somas de variáveis multiplicadas por constantes, sem potências, produtos entre variáveis ou funções não lineares.

Um problema típico de maximização, na forma padrão, é:

$$
\begin{aligned}
\text{Max } & Z = c_1x_1 + c_2x_2 + \dots + c_nx_n \\
\text{sujeito a } & a_{i1}x_1 + a_{i2}x_2 + \dots + a_{in}x_n \;(\le,\ \ge,\ =)\; b_i \quad (i = 1,\dots,m) \\
& x_1, \dots, x_n \ge 0
\end{aligned}
$$

> **Importante:** o método assume que todas as variáveis de decisão são **não negativas** ($x_j \ge 0$).

## 2. A intuição geométrica

Com apenas duas variáveis, o problema pode ser desenhado em um plano. Cada restrição delimita um semiplano, e a interseção de todos eles forma a **região viável**: o conjunto de pontos que respeitam todas as regras ao mesmo tempo.

A propriedade que torna a PL tratável é esta:

> Se o problema possui solução ótima, **ao menos um vértice** (canto) da região viável é ótimo.

Ou seja, não é necessário examinar o interior da região. Em alguns casos, uma aresta inteira empata no melhor valor (são as **múltiplas soluções ótimas**), mas mesmo assim os vértices que a delimitam também são ótimos.

## 3. Por que o Simplex

Com 2 variáveis é fácil inspecionar os vértices no gráfico. Com 50 variáveis e 100 restrições, a região viável existe em um espaço de 50 dimensões, e o número de vértices cresce de forma explosiva, o que torna inviável testá-los todos.

O **Simplex** evita essa busca exaustiva ao caminhar de vértice em vértice **sempre sem piorar** o objetivo:

1. Parte de um vértice inicial (normalmente a origem, com as variáveis de decisão iguais a zero).
2. Escolhe a direção em que o objetivo melhora mais depressa (**coluna pivô**).
3. Avança por essa aresta até o limite imposto pela restrição mais próxima, sem violá-la (**teste da razão** e **linha pivô**).
4. Repete a partir do novo vértice até que nenhuma direção melhore o resultado. Nesse ponto, a solução é ótima.

## 4. Da desigualdade à igualdade: variáveis de folga, excesso e artificiais

O algoritmo trabalha com **sistemas de equações**, não de desigualdades. Por isso, cada restrição é convertida em uma igualdade com o auxílio de variáveis extras, todas também não negativas.

| Tipo da restrição | Transformação | Variáveis adicionadas | Variável inicial na base |
|---|---|---|---|
| $\le$ | $\dots + s = b$ | 1 variável de **folga** ($s$) | $s$ |
| $\ge$ | $\dots - s + a = b$ | 1 variável de **excesso** ($s$, com sinal $-$) e 1 **artificial** ($a$) | $a$ |
| $=$ | $\dots + a = b$ | 1 **artificial** ($a$) | $a$ |

- **Folga:** mede quanto de um recurso *sobrou*. Se há 100 horas disponíveis e 80 foram usadas, a folga é 20.
- **Excesso:** mede quanto a solução *ultrapassou* o mínimo exigido. Se são exigidas 40.000 requisições e foram atendidas 45.000, o excesso é 5.000.
- **Artificial:** não representa nada do problema real. É um artifício para obter uma solução inicial (ver [seção 8](#8-o-método-big-m)).

**Lado direito negativo.** O método exige $b_i \ge 0$. Quando uma restrição tem lado direito negativo, o solver a multiplica por $-1$ e **inverte o sentido** da desigualdade:

| Original | Normalizada |
|---|---|
| $-x_1 \le -2$ | $x_1 \ge 2$ |
| $-x_3 \ge -4$ | $x_3 \le 4$ |
| $-x_2 = -1$ | $x_2 = 1$ (a igualdade permanece) |

## 5. O Tableau

O **Tableau** é a matriz que armazena o sistema de equações e o estado do algoritmo. No projeto, as colunas seguem a ordem:

```
Z | x1 ... xn | s1 ... sk | a1 ... ap | RHS
```

- **Primeira coluna:** a variável objetivo (`Z` na maximização, `-Z` na minimização).
- **Colunas `x`, `s` e `a`:** variáveis de decisão, de folga/excesso e artificiais.
- **Última coluna (`RHS`, *Right-Hand Side*):** o lado direito das equações.
- **Primeira linha (linha Z):** os coeficientes da função objetivo reescrita, também chamados de **custos reduzidos**. Na maximização, o solver coloca os coeficientes com **sinal trocado** ($-c_j$).
- **Demais linhas:** uma por restrição. O nome de cada linha é a **variável básica** daquela equação.

### Variáveis básicas e não básicas

Em qualquer momento, o sistema tem mais variáveis que equações. O algoritmo escolhe, para cada equação, uma variável que a "representa": a **variável básica**. Todas as outras são **não básicas** e valem **zero**.

- Isso significa que *básica* e *não básica* é um **estado**, que muda a cada iteração, e não um tipo de variável.
- O valor de uma variável básica é lido no `RHS` da sua linha.
- Uma variável básica **pode valer zero** (situação chamada de *degeneração*).

### Leitura de um tableau

Considere o tableau inicial do problema `Max Z = 3x₁ + 5x₂` com as restrições `x₁ ≤ 4`, `2x₂ ≤ 12` e `3x₁ + 2x₂ ≤ 18`:

```
        Z    x1    x2    s1    s2    s3    RHS
Z       1    -3    -5     0     0     0      0     ← linha Z (custos reduzidos)
s1      0     1     0     1     0     0      4     ← s1 = 4
s2      0     0     2     0     1     0     12     ← s2 = 12
s3      0     3     2     0     0     1     18     ← s3 = 18
```

Leitura:

- A base inicial é `{s1, s2, s3}`. Portanto, `s1 = 4`, `s2 = 12`, `s3 = 18` e as demais variáveis valem 0.
- Isso corresponde ao vértice **(0, 0)**, com $Z = 0$.
- Os valores `-3` e `-5` na linha Z indicam que **aumentar** $x_1$ ou $x_2$ melhora o objetivo. Quanto mais negativo, mais rápida a melhora por unidade.

## 6. Uma iteração: pivô de coluna, pivô de linha e pivoteamento

Cada iteração executa três passos.

### Passo 1: escolher a coluna pivô (variável que entra na base)

Escolhe-se a coluna da linha Z com o **coeficiente mais negativo**. Esse critério é conhecido como **regra de Dantzig**. Se não houver nenhum coeficiente negativo, o algoritmo termina: não há mais direção que melhore o objetivo.

### Passo 2: escolher a linha pivô (variável que sai da base): teste da razão

Para cada linha em que o valor da coluna pivô é **estritamente positivo**, calcula-se a razão:

$$
\text{razão}_i = \frac{\text{RHS}_i}{\text{valor da coluna pivô na linha } i}
$$

A linha de **menor razão** é a linha pivô. Isso garante que a variável que entra cresça apenas até o limite da primeira restrição que se esgota, mantendo **todos os RHS não negativos** (solução viável).

Se **nenhuma** linha possui valor positivo na coluna pivô, a variável pode crescer indefinidamente sem violar restrições: o problema é **ilimitado** (ou inviável, caso haja artificial positiva na base; ver [seção 10](#10-interpretação-dos-resultados)).

### Passo 3: pivotear

O **elemento pivô** é a interseção da linha e da coluna pivô. O pivoteamento é uma eliminação de Gauss-Jordan que faz a coluna pivô virar uma coluna da matriz identidade:

1. **Linha pivô:** divide-se toda a linha pelo elemento pivô.
2. **Demais linhas** (inclusive a linha Z):

$$
\text{nova linha}_i = \text{linha}_i - \big(\text{valor da linha } i \text{ na coluna pivô}\big) \times \big(\text{linha pivô já dividida}\big)
$$

3. A variável da coluna pivô entra na base no lugar da variável da linha pivô.

## 7. Exemplo completo resolvido

Problema: `Max Z = 3x₁ + 5x₂` sujeito a `x₁ ≤ 4`, `2x₂ ≤ 12`, `3x₁ + 2x₂ ≤ 18`.

**Iteração 0 (tableau inicial, vértice (0, 0), Z = 0).** Mostrado na [seção 5](#leitura-de-um-tableau).

**Iteração 1.**

- Coluna pivô: `x2` (−5 é o mais negativo).
- Razões: `s1` não participa (valor 0); `s2`: 12 ÷ 2 = **6**; `s3`: 18 ÷ 2 = 9. Linha pivô: `s2`.
- `x2` entra e `s2` sai. Linha pivô dividida por 2; linha Z recebe `+5 ×` a linha pivô; `s3` recebe `−2 ×` a linha pivô.

```
        Z    x1    x2    s1    s2    s3    RHS
Z       1    -3     0     0   5/2     0     30
s1      0     1     0     1     0     0      4
x2      0     0     1     0   1/2     0      6
s3      0     3     0     0    -1     1      6
```

Vértice **(0, 6)**, com $Z = 30$.

**Iteração 2.**

- Coluna pivô: `x1` (−3).
- Razões: `s1`: 4 ÷ 1 = 4; `s3`: 6 ÷ 3 = **2**. Linha pivô: `s3`.
- `x1` entra e `s3` sai.

```
        Z    x1    x2    s1    s2    s3    RHS
Z       1     0     0     0   3/2     1     36
s1      0     0     0     1   1/3  -1/3      2
x2      0     0     1     0   1/2     0      6
x1      0     1     0     0  -1/3   1/3      2
```

Não há coeficientes negativos na linha Z: **fim**.

**Resultado:** `x₁ = 2`, `x₂ = 6`, `s₁ = 2`, `s₂ = 0`, `s₃ = 0` e `Z = 36`. A folga `s₁ = 2` indica que a restrição `x₁ ≤ 4` não foi totalmente usada (2 < 4). As restrições 2 e 3 estão no limite (folga zero).

## 8. O método Big-M

Quando todas as restrições são do tipo $\le$ com $b \ge 0$, a base inicial é formada pelas folgas e a origem é um vértice viável. Já com restrições $\ge$ ou $=$, a origem **não** é viável, e não há base inicial óbvia.

O **Big-M** resolve isso com as variáveis artificiais:

1. Cada restrição $\ge$ ou $=$ recebe uma artificial $a$, que forma a base inicial dessa linha.
2. Cada artificial entra na função objetivo com um **custo $M$**, um valor imaginário **arbitrariamente maior que qualquer número real do problema**. Como a penalidade é altíssima, o algoritmo é "forçado" a expulsar as artificiais da base.
3. Se, ao final, todas as artificiais saíram (ou ficaram com valor zero), a solução encontrada é viável para o problema original. Se alguma permanecer **positiva**, as restrições são contraditórias: o problema é **inviável**.

### M é simbólico

O valor de $M$ **nunca é fixado** (por exemplo, em 1.000.000), o que evitaria erros numéricos. Em vez disso, cada coeficiente é guardado como um par **(valor real, multiplicador de M)** na classe `BigMCoefficient`:

| Notação no tableau | `value` | `bigM` |
|---|---|---|
| `3` | 3 | 0 |
| `M` | 0 | 1 |
| `3-4M` | 3 | −4 |
| `-1+4/3M` | −1 | 4/3 |

A **comparação é lexicográfica**: primeiro compara-se a parte de $M$ e só em caso de empate compara-se a parte real. Isso reflete que $M$ é "infinitamente maior" que qualquer real: `-1M` é menor que `-1000` e `3-4M` é menor que `2-2M`, porque $-4 < -2$.

### Zerar as artificiais na linha Z

As artificiais começam na base, mas aparecem na linha Z com coeficiente $M$. Para que a linha Z fique coerente (custo reduzido zero para variáveis básicas), é preciso **eliminar esse $M$** subtraindo, da linha Z, $M \times$ a linha de cada artificial. No código, isso é o método `resetZRow`.

### Exemplo mínimo

`Min Z = x₁` sujeito a `x₁ ≥ 2`. A restrição vira `x₁ − s₁ + a₁ = 2`.

Tableau inicial (a linha Z da minimização usa os coeficientes originais, e `a1` entra com custo `M`):

```
        -Z    x1    s1    a1    RHS
-Z       1     1     0     M      0
a1       0     1    -1     1      2
```

Após `resetZRow` (linha Z − M × linha de `a1`):

```
        -Z    x1    s1    a1    RHS
-Z       1   1-M     M     0    -2M
a1       0     1    -1     1      2
```

- Coluna pivô: `x1` (`1-M` é estritamente negativo, pois a parte $M$ vale −1).
- Linha pivô: `a1` (única com valor positivo; razão 2).
- Após o pivoteamento:

```
        -Z    x1    s1     a1    RHS
-Z       1     0     1   -1+M     -2
x1       0     1    -1      1      2
```

Nenhum coeficiente é estritamente negativo ($-1+M$ tem parte $M$ positiva). A artificial saiu da base: solução viável e ótima, com `x₁ = 2`. O valor objetivo é $-(-2) = 2$.

## 9. Minimização

Minimizar $Z$ equivale a **maximizar $-Z$**. Por isso, nos problemas de minimização:

- A coluna do objetivo chama-se `-Z`.
- A linha Z é montada com os coeficientes **sem troca de sinal**, e o restante do algoritmo é idêntico.
- O valor final de $Z$ é o `RHS` da linha Z **multiplicado por −1**.

Essa abordagem substitui a antiga ideia de resolver o **dual**, que não faz parte desta versão.

## 10. Interpretação dos resultados

O solver devolve um status que resume o veredito matemático:

| Status | Significado | Como o algoritmo detecta |
|---|---|---|
| **OPTIMAL** | Solução ótima única. | Nenhum coeficiente estritamente negativo na linha Z e nenhuma artificial positiva na base. |
| **MULTIPLE_OPTIMAL** | Há mais de uma solução com o mesmo valor ótimo. | Uma variável de decisão `x` **não básica** tem custo reduzido zero: ela pode entrar na base sem alterar $Z$. |
| **UNBOUNDED** | O objetivo pode crescer indefinidamente. Em problemas reais, geralmente indica erro de modelagem. | A coluna pivô não tem nenhum valor positivo (teste da razão sem linha elegível) e não há artificial positiva na base. |
| **INFEASIBLE** | As restrições se contradizem; não existe solução. | Há uma variável artificial **positiva** na base. Exemplo: "gastar no máximo R$ 100" e "o mínimo exigido custa R$ 150". |
| **DEGENERATE_CYCLING** | O limite de 1000 iterações foi atingido sem convergir. | Lançada como `SimplexConvergenceException` e tratada pelo `GlobalExceptionHandler`. |

> **Sobre o último status:** o nome sugere degeneração, mas o fato comprovado é apenas que o limite de iterações foi excedido. A causa mais provável é *ciclagem* em um problema degenerado (ver seção seguinte).

**Valores das variáveis em problemas ilimitados.** Quando o status é `UNBOUNDED`, os valores exibidos correspondem ao **último vértice visitado**, e não a uma solução ótima.

## 11. Limitações conhecidas

- **Sem regra de Bland.** A coluna pivô é escolhida pela regra de Dantzig. Em problemas degenerados específicos, o algoritmo pode *ciclar* (voltar a um tableau já visitado indefinidamente). O projeto apenas impõe o limite de 1000 iterações. O teste com o exemplo clássico de Chvátal documenta esse comportamento.
- **`MULTIPLE_OPTIMAL` considera apenas variáveis `x`.** Variáveis de folga ou excesso não básicas com custo reduzido zero não são examinadas.
- **Variáveis não negativas.** O modelo não suporta variáveis livres (que podem ser negativas).
- **Validação parcial.** O formulário gera campos consistentes, mas um envio manual com restrições incompletas pode causar erro.
- **Tolerância numérica.** Comparações usam $\varepsilon = 10^{-9}$ e a exibição converte decimais em frações quando possível. Problemas muito grandes ou mal condicionados podem sofrer com erros de ponto flutuante.

## 12. Glossário

| Termo | Definição |
|---|---|
| **Função objetivo** | Expressão a ser maximizada ou minimizada. |
| **Restrição** | Condição (≤, ≥ ou =) que a solução deve respeitar. |
| **Região viável** | Conjunto de pontos que satisfazem todas as restrições. |
| **Vértice** | "Canto" da região viável; candidato a solução ótima. |
| **Variável de decisão** (`x`) | Incógnita do problema real. |
| **Variável de folga** (`s`, sinal +) | Quanto sobrou de um recurso em uma restrição ≤. |
| **Variável de excesso** (`s`, sinal −) | Quanto foi ultrapassado de um mínimo exigido em uma restrição ≥. |
| **Variável artificial** (`a`) | Variável auxiliar do Big-M, sem significado real, que deve terminar zerada. |
| **Base** | Conjunto de variáveis básicas, uma por restrição. |
| **Variável básica / não básica** | Variável que está na base (pode ter valor positivo) / que está fora dela (vale zero). |
| **Tableau** | Matriz que representa o sistema de equações a cada iteração. |
| **Custo reduzido** | Coeficiente da linha Z; indica o ganho (ou perda) por unidade ao aumentar uma variável. |
| **Coluna pivô** | Coluna da variável que entra na base. |
| **Linha pivô** | Linha da variável que sai da base. |
| **Elemento pivô** | Interseção da linha e da coluna pivô. |
| **Teste da razão** | Cálculo `RHS ÷ valor da coluna pivô` que define a linha pivô. |
| **Big-M** | Técnica que penaliza as artificiais com um custo `M` arbitrariamente grande. |
| **Degeneração** | Situação em que uma variável básica vale zero. |
| **Ciclagem** | Repetição indefinida de tableaux, possível em problemas degenerados. |
| **RHS** | *Right-Hand Side*: coluna do lado direito das equações. |

## 13. Mapa entre teoria e código

| Conceito | Onde está no código |
|---|---|
| Modelo do problema (objetivo, restrições, tipo de otimização) | `SimplexProblem`, `Constraint`, `ConstraintType`, `OptimizationType` |
| Conversão para igualdades e normalização de RHS negativo | `SimplexSolver#createTableau` |
| Tableau e pivoteamento | `Tableau`, `Tableau#pivot` |
| Coeficientes do tipo `a + bM` e comparação lexicográfica | `BigMCoefficient` |
| Eliminação do `M` das artificiais na linha Z | `SimplexSolver#resetZRow` |
| Escolha da coluna pivô (Dantzig) | `SimplexSolver#findPivotColumn` |
| Teste da razão e escolha da linha pivô | `SimplexSolver#findPivotRow` |
| Laço principal e detecção de `OPTIMAL`, `UNBOUNDED` e `INFEASIBLE` | `SimplexSolver#iterate` |
| Detecção de `MULTIPLE_OPTIMAL` e montagem do resultado | `SimplexSolver#getResult` |
| Registro passo a passo exibido na página | `Iteration` |
| Veredito final | `StatusResult`, `SimplexResult` |
| Limite de iterações | `SimplexConvergenceException`, `GlobalExceptionHandler` |

---

Para experimentar na prática, acesse a rota `/simplex` da aplicação e preencha o formulário com o seu próprio problema de otimização.
