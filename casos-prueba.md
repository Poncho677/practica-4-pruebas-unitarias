### Casos normales
| Casos | Entrada | Resultado Esperado | Regla | Justificación |
|---|---|---|---|---|
| 1 | 10 min, normal, boleto no perdido | $0.00 | R5 | 10 min está dentro de "1 a 15": no se cobra |
| 2 | 45 min, normal, boleto no perdido | $20.00 | R6 | 45 min está a mitad de "16 a 60": se cobra la tarifa de la primera hora |
| 3 | 90 min, normal, boleto no perdido | $35.00 | R7 | $20 de la primera hora más $15 de una hora adicional iniciada |
 
### Casos frontera
| Casos | Entrada | Resultado Esperado | Regla | Justificación |
|---|---|---|---|---|
| 1 | 0 min, normal, boleto no perdido | $0.00 | R4 | 0 minutos: gratis |
| 2 | 1 min, normal, boleto no perdido | $0.00 | R5 | Primer valor del rango "1 a 15" |
| 3 | 14 min, normal, boleto no perdido | $0.00 | R5 | Justo antes del límite de 15 |
| 4 | 15 min, normal, boleto no perdido | $0.00 | R5 | Último minuto gratis ("de 1 a 15") |
| 5 | 16 min, normal, boleto no perdido | $20.00 | R6 | Primer minuto que ya se cobra: cambia de $0 a $20 |
| 6 | 17 min, normal, boleto no perdido | $20.00 | R6 | Justo después del límite |
| 7 | 60 min, normal, boleto no perdido | $20.00 | R6 | Último minuto del rango "16 a 60" |
| 8 | 120 min, normal, boleto no perdido | $35.00 | R7 | Exactamente 1 hora adicional (60 + 60) |
| 9 | 121 min, normal, boleto no perdido | $50.00 | R7 | Primera hora + 2 horas adicionales iniciadas |
 
### Entradas inválidas
| Casos | Entrada | Resultado Esperado | Regla | Justificación |
|---|---|---|---|---|
| 1 | -1 min, normal, boleto no perdido | ERROR (ValueError) | R1 y R10 | Minutos negativos |
| 2 | 30 min, Normal, boleto no perdido | ERROR (ValueError) | R2 y R11 | Los valores válidos son exactamente "normal" y "frecuente" (distingue mayúsculas) |

### Interacción entre reglas
| Casos | Entrada | Resultado Esperado | Reglas | Justificación |
|---|---|---|---|---|
| 1 | 61 min, frecuente, boleto no perdido | $31.50 | R7 y R8 | Más de 60 min con cliente frecuente: $35.00 menos el 10 % |
| 2 | 15 min, frecuente, boleto no perdido | $0.00 | R5 y R8 | Frontera de 15 min combinada con descuento: 0 menos 10 % sigue siendo 0 |

 
### Números decimales
| Casos | Entrada | Resultado Esperado | Reglas | Justificación |
|---|---|---|---|---|
| 1 | 181 min, frecuente, boleto no perdido | $58.50 | R7 y R8 | $65.00 menos el 10 % da un resultado con decimales; se compara con tolerancia (pytest.approx) |

 
### Caso adicional
| Casos | Entrada | Resultado Esperado | Reglas | Justificación |
|---|---|---|---|---|
| 1 | Cualquier valor de 0 a 600 min, ambos tipos, con y sin boleto perdido | Resultado mayor o igual a $0.00 | R9 | El costo nunca puede ser negativo |
