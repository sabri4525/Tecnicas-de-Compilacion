# -*- coding: utf-8 -*-
"""Practico10-10-24.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1XG5XFs2DsML0jThgzTm-m6x1Ib39oXtT

Ejercicio 1
"""

numero = int(input("Introduce un numero: "))
if numero%2==0:
  print (f"{numero} es par.")
else:
  print(f"{numero} es impar.")

"""Ejercicio 2"""

suma=0
while numero>=0:
  numero = int(input("Introduce un numero: "))
  suma = suma + numero
print(suma)

"""Ejercicio 3"""

word = (input("Introduce una palabra: "))
for letter in word:
  print (letter)

"""Ejercicio 4"""

dia = int(input("Introduce un dia de la semana(1-7): "))
match dia:
  case 0 | None:
    print ("No se ingreso un numero valido")
  case 1:
    print("Lunes")
  case 2:
    print("Martes")
  case 3:
    print("Miercoles")
  case 4:
    print("Jueves")
  case 5:
    print("Viernes")
  case 6:
    print("Sabado")
  case 7:
    print("Domingo")
  case _:
    print ("No se ingreso un numero valido")

"""Ejercicio 5"""

frase = (input("Introduce una frase: "))
frase_invertida = ""
palabra_invertida = ""

for i in range(len(frase)):
  if frase[i] == " ":
    frase_invertida += palabra_invertida + " "
    palabra_invertida = ""
  else:
    palabra_invertida = frase[i] + palabra_invertida

frase_invertida += palabra_invertida

print("Frase invertida:", frase_invertida)

"""Ejercicio 6"""

numero = int(input("Ingresa un número: "))

if numero < 1:
  print("Error: El número debe ser mayor o igual a 1.")
else:
  suma = 0
  for i in range(2, numero + 1, 2):
    suma += i
  print("La suma de los números pares desde 1 hasta", numero, "es:", suma)

"""Ejercicio 7"""

def suma_lista(lista):
  suma = 0
  for elemento in lista:
    suma += elemento
  return suma
lista = [1, 2, 3, 4, 5]
print(suma_lista(lista))

"""Ejercicio 8"""

def maximo_lista(lista):
  if not lista:
    return None

  maximo = lista[0]
  for elemento in lista:
    if elemento > maximo:
      maximo = elemento
  return maximo

lista = [1, 2, 3, 4, 5]
print(maximo_lista(lista))

"""Ejercicio 9"""

def invertir_lista(lista):
  lista_invertida = []
  for i in range(len(lista) - 1, -1, -1):
    lista_invertida.append(lista[i])
  return lista_invertida

lista = [1, 2, 3, 4, 5]
print(invertir_lista(lista))

"""Ejercicio 10"""

def eliminar_duplicados(lista):

  lista_sin_duplicados = []
  for elemento in lista:
    if elemento not in lista_sin_duplicados:
      lista_sin_duplicados.append(elemento)
  return lista_sin_duplicados

lista = [1, 2, 2, 3, 4, 4, 5]
print(eliminar_duplicados(lista))

"""Ejercicio 11"""

def interseccion_listas(lista1, lista2):

  interseccion = []
  for elemento in lista1:
    if elemento in lista2:
      interseccion.append(elemento)
  return interseccion

lista1 = [1, 2, 3, 4, 5]
lista2 = [4, 5, 6, 7, 8]
print(interseccion_listas(lista1, lista2))

"""Ejercicio 12"""

def union_listas(lista1, lista2):

  union = []
  for elemento in lista1:
    if elemento not in union:
      union.append(elemento)
  for elemento in lista2:
    if elemento not in union:
      union.append(elemento)
  return union

lista1 = [1, 2, 3, 4, 5]
lista2 = [4, 5, 6, 7, 8]
print(union_listas(lista1, lista2))

"""Ejercicio 13"""

def ordenar_lista(lista):

  lista_ordenada = sorted(lista)
  return lista_ordenada


lista = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5]
print(ordenar_lista(lista))

"""Ejercicio 14"""

def contar_apariciones(lista, elemento):

  contador = 0
  for item in lista:
    if item == elemento:
      contador += 1
  return contador

lista = [1, 2, 3, 1, 2, 1, 1, 4, 5]
elemento = 1
print(contar_apariciones(lista, elemento))

"""Ejercicio 15"""

def eliminar_elemento(lista, elemento):

  lista_sin_elemento = []
  for item in lista:
    if item != elemento:
      lista_sin_elemento.append(item)
  return lista_sin_elemento

lista = [1, 2, 3, 4, 5, 3]
elemento = 3
print(eliminar_elemento(lista, elemento))

"""Ejercicio 16"""

def lista_cuadrados(n):

  cuadrados = []
  for i in range(1, n + 1):
    cuadrados.append(i * i)
  return cuadrados

n = 5
print(lista_cuadrados(n))