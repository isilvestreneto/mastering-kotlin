package proway.capgemini.atividades

import java.time.LocalDate
import java.util.Locale
import java.util.Scanner

val VALORES_PERMITIDOS = arrayOf("A", "B", "C", "D", "E", "F");

val leitura = Scanner(System.`in`);

val pets: Array<Pet> =

fun main() {
    printInitialLogo();

    runMenu()
}

private fun runMenu() {
    val entrada = catchMenu();

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
    TODO("Not yet implemented")
}

fun alterar() {
    TODO("Not yet implemented")
}

fun pesquisar() {
    TODO("Not yet implemented")
}

fun listar() {
    TODO("Not yet implemented")
}

fun cadastrar() {
    println("Iniciando cadastro de pet...")
    println("Informe os dados da pet:")
    println("Nome:")
    val nome = leitura.nextLine();
    println("Data de nascimento: (formato DD/MM/AAAA | Exemplo: \"30/09/1995\")")
    val dataNascimento = leitura.nextLine();

    println("Tipo: (digite a letra correspondente)")
    // Aqui vai questões A B C D para tipos: canino, felino, roedor, ave, etc.
    println("A) Canino \n B) Felino \n C) Roedor \n D) Felino")
    val raca = leitura.nextLine();

    when (raca) {
        "A" -> {
            // Montar objeto canino
            val pet = Canino(nome, dataNascimento);
        };
        "B" -> {
            val pet = Felino(nome, dataNascimento);
        };
        "C" -> {
            val pet = Roedor(nome, dataNascimento);
        };
        "D" -> {
            val pet = Ave(nome, dataNascimento);
        };
        else -> {
            println("Opção inválida, tente novamente.");
            cadastrar();
        }
    }

    persistirDado(pet);

    println("Pet cadastrado com sucesso!");
}

fun persistirDado(pet: Pet) {

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

    print("Entre com uma opção:")
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
    val dataNascimento: String
    val tipo: Tipo
}

// Usando data class para facilitar comparação e log (toString)
data class Canino(override val nome: String, override val dataNascimento: String) : Pet {
    override val tipo = Tipo.CANINO
}

data class Felino(override val nome: String, override val dataNascimento: String) : Pet {
    override val tipo = Tipo.FELINO
}

data class Roedor(override val nome: String, override val dataNascimento: String) : Pet {
    override val tipo = Tipo.ROEDOR
}

data class Ave(override val nome: String, override val dataNascimento: String) : Pet {
    override val tipo = Tipo.AVE
}

enum Tipo {
    CANINO, FELINO, ROEDOR, AVE
}


/**
 * *Projeto - Introdução ao Desenvolvimento com Kotlin*
 *
 * Criar um projeto contendo as seguintes características:
 *
 * 1. Deverá ter uma classe de modelo;
 *
 * 2. Crie uma estrutura de lista para gerenciar os objetos oriundos da classe;
 *
 * 3. Deverá ter as seguintes funcionalidades:
 * A) Cadastrar;
 * B) Listar;
 * C) Pesquisar;
 * D) Alterar;
 * E) Remover;
 * F) Finalizar.
 *
 * 4. Deverá ter o uso de pelo menos um Null Safety;
 *
 * 5. Implemente pelo menos um Elvis Operator;
 *
 * 6. Estruture utilizando os conceitos de POO;
 *
 * 7. Realize validações para garantir registros coesos;
 *
 * 8. Para criar essa interação com o usuário, utilize a classe Scanner.
 *
 */