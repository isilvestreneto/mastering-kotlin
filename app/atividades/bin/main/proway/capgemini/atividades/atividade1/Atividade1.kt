package proway.capgemini.atividades.atividade1

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import java.util.Scanner

val VALORES_PERMITIDOS = arrayOf("A", "B", "C", "D", "E", "F");

val TIPOS_ALTERNATIVAS = arrayOf("A", "B", "C", "D", "E");
val TIPOS_ALTERNATIVAS_ATUALIZACAO = arrayOf("A", "B", "C", "D");

val leitura = Scanner(System.`in`);

val pets: MutableList<Pet> = mutableListOf();

fun main() {
    printInitialLogo();

    runMenu()
}

private fun runMenu() {
    var entrada = catchMenu();
    val entradaUppercase = entrada.uppercase(Locale.ROOT);

    if (!VALORES_PERMITIDOS.contains(entradaUppercase)) {
        println("Opção inválida, tente novamente.");
        runMenu();
    } else {
        while (entrada != "F") {
            when (entrada) {
                "A" -> cadastrar();
                "B" -> listar();
                "C" -> pesquisar();
                "D" -> alterar();
                "E" -> remover();
            }
            entrada = catchMenu();
        }
        finalizar();
    }
}

private fun catchMenu(): String {
    printMenu();
    val entrada = leitura.nextLine();
    return entrada
}

fun remover() {
    if (pets.isEmpty()) {
        println("Nenhum pet cadastrado.");
        return;
    }

    println("Informe o nome do pet que deseja remover:")
    val nome = leitura.nextLine();
    val petEncontrado = pets.find { it.nome.equals(nome, ignoreCase = true); }

    if (petEncontrado == null) {
        println("Nenhum pet encontrado...");
        return;
    }
    pets.remove(petEncontrado);
    println("Pet removido com sucesso!");
}

fun alterar() {
    println("Iniciando alteração de pet...")
    if (pets.isEmpty()) {
        println("Nenhum pet cadastrado.");
        return;
    }

    println("Informe o nome do pet que deseja alterar:")
    val nome = leitura.nextLine();
    val petEncontrado = pets.find { it.nome.equals(nome, ignoreCase = true) }

    if (petEncontrado == null) {
        println("Nenhum pet encontrado...");
        return;
    }

    println("Pet do tipo ${petEncontrado.tipo} encontrado: ${petEncontrado.nome} - Nascido em ${petEncontrado.dataNascimento}")
    println("Informe os novos dados do pet:")
    println("Nome:")
    val novoNome = validarNome(leitura);
    println("Data de nascimento: (formato DD/MM/AAAA | Exemplo: \"30/09/1995\")")
    val novaDataNascimento = parseLocalDate(leitura);
    println("Tipo: (digite a letra correspondente)")
    var novoTipo: String;
    do {
        println("A) Canino \nB) Felino \nC) Roedor \nD) Ave")

        novoTipo = leitura.nextLine();

        if (!TIPOS_ALTERNATIVAS_ATUALIZACAO.contains(novoTipo.uppercase(Locale.ROOT))) {
            println("Opção inválida, tente novamente.");
        }
    } while (!TIPOS_ALTERNATIVAS_ATUALIZACAO.contains(novoTipo.uppercase(Locale.ROOT)));

    val petAlterado: Pet = when (novoTipo.uppercase(Locale.ROOT)) {
        "A" -> Canino(novoNome, novaDataNascimento);
        "B" -> Felino(novoNome, novaDataNascimento);
        "C" -> Roedor(novoNome, novaDataNascimento);
        "D" -> Ave(novoNome, novaDataNascimento);
        else -> {
            throw IllegalArgumentException("Tipo inválido")
        }
    }

    // Substitui o pet antigo pelo novo
    val index = pets.indexOf(petEncontrado)
    pets[index] = petAlterado;

    println("Pet alterado com sucesso!");
}

fun validarNome(scanner: Scanner): String {
    var nome: String;

    do {
        nome = scanner.nextLine();
        if (nome.isBlank()) {
            println("Nome não pode ser vazio, tente novamente.");
        }
    } while (nome.isBlank());

    return nome;
}

fun parseLocalDate(scanner: Scanner): LocalDate {
    var data: LocalDate? = null;
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    do {
        val input = scanner.nextLine();
        try {
            data = LocalDate.parse(input, formatter);
        } catch (e: DateTimeParseException) {
            println("Data inválida, tente novamente. Formato esperado: DD/MM/AAAA");
        }
    } while (data == null);

    return data;
}

fun pesquisar() {
    println("Pesquisando pet...")

    if (pets.isEmpty()) {
        println("Nenhum pet cadastrado.");
        return;
    }

    println("Informe o nome do pet que deseja pesquisar:")
    val nome = leitura.nextLine();

    val petEncontrado = pets.find { it.nome.equals(nome, ignoreCase = true) }

    petEncontrado?.let {
        println("Pet do tipo ${it.tipo} encontrado: ${it.nome} - Nascido em ${it.dataNascimento}")
    } ?: println("Nenhum pet encontrado...")

}

fun listar() {
    println("Listando pets cadastrados...")

    if (pets.isEmpty()) {
        println("Nenhum pet cadastrado.");
    } else {
        pets.forEachIndexed { index, pet ->
            println("${index + 1} - ${pet.nome} (${pet.tipo}) - Nascido em ${pet.dataNascimento}")
        }
    }
}

fun cadastrar() {
    println("Iniciando cadastro de pet...")
    println("Informe os dados da pet:")
    println("Nome:")
    val nome = validarNome(leitura);
    println("Data de nascimento: (formato DD/MM/AAAA | Exemplo: \"30/09/1995\")")
    val dataNascimento = parseLocalDate(leitura);

    println("Tipo: (digite a letra correspondente)")


    var tipo: String;

    do {
        println("A) Canino \nB) Felino \nC) Roedor \nD) Ave \nE) Retornar ao menu")

        tipo = leitura.nextLine();

        if (!TIPOS_ALTERNATIVAS.contains(tipo.uppercase(Locale.ROOT))) {
            println("Opção inválida, tente novamente.");
        }
    } while (!TIPOS_ALTERNATIVAS.contains(tipo.uppercase(Locale.ROOT)));

    if (tipo.uppercase(Locale.ROOT) == "E") {
        return;
    }

    val pet: Pet = when (tipo) {
        "A" -> Canino(nome, dataNascimento);
        "B" -> Felino(nome, dataNascimento);
        "C" -> Roedor(nome, dataNascimento);
        "D" -> Ave(nome, dataNascimento);
        else -> {
            throw IllegalArgumentException("Tipo inválido")
        }
    }

    persistirDado(pet);
}

fun persistirDado(pet: Pet) {
    pets.add(pet);
    println("Pet cadastrado com sucesso!");
}

fun finalizar() {
    println("Finalizando...");
    return;
}

fun printMenu() {
    println("MENU")
    println("A - Cadastrar")
    println("B - Listar")
    println("C - Pesquisar")
    println("D - Alterar")
    println("E - Remover")
    println("F - Finalizar")
    println("")

    print("Entre com uma opcão:")
}

private fun printInitialLogo() {
    print(
        """
           _   _ _       
  /\ /\___ | |_| (_)_ __  
 / //_/ _ \| __| | | '_ \ 
/ __ \ (_) | |_| | | | | |
\/  \/\___/ \__|_|_|_| |_|
                          
      """.trimIndent()
    )

    println("");
}

interface Pet {
    val nome: String
    val dataNascimento: LocalDate
    val tipo: Tipo
}

// Usando data class para facilitar comparação e log (toString)
data class Canino(override val nome: String, override val dataNascimento: LocalDate) : Pet {
    override val tipo = Tipo.CANINO
}

data class Felino(override val nome: String, override val dataNascimento: LocalDate) : Pet {
    override val tipo = Tipo.FELINO
}

data class Roedor(override val nome: String, override val dataNascimento: LocalDate) : Pet {
    override val tipo = Tipo.ROEDOR
}

data class Ave(override val nome: String, override val dataNascimento: LocalDate) : Pet {
    override val tipo = Tipo.AVE
}

enum class Tipo {
    CANINO, FELINO, ROEDOR, AVE
}