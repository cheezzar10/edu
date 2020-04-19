.text

.global __start
__start:

# iconst_1
# reserving 4 bytes on stack for int
addi sp, sp, -4
# register t0 = 1
li t0, 1
# saving t0 on top of the stack
sw t0, 0(sp)

# istore_1

# popping from the stack to local
lw t0, 0(sp)
addi sp, sp, 4

# returning to OS
li a0, 10
ecall