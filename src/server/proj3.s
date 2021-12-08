# The Dangerous World of Competitive, High Stakes Checkers!
# Authors: Aidan Kirk, Gabe
# Date: 11/17/21
# Description: This project takes an m by n checkerboard, each cell with a cost
# value, and recursively runs to find the costliest path from the first row to
# the last with diagonal movement. It then prints the costliest starting col and
# it's summed cost.

.data

	row:    .word 0
	hold:    .word 0
	output1:      .asciiz "The max value is "
	output2:      .asciiz "starting at output2 "
	newLine:          .byte 10 0

.text

.globl play_checkers

play_checkers:

    # load row and hold into data
    sw    $a1, row
    sw    $a2, hold

    # Calculating the memory we need to store board
    mul   $t0, $a1, $a2       # row * col 
    li    $t1, 4
    mul   $t2, $t0, $t1       # space for board = row * col * 4
    sub   $sp, $sp, $t2       # stack pointer = stack pointer - space
    
    # Setup for loop1 - writing the board to our stack
    li    $t1, 0

loop1:

    add   $t3, $t1, $t1       # i + i
    add   $t3, $t3, $t3       # 2i + 2i
    add   $t4, $t3, $a0       # calc address of array[i]
        
    add   $t2, $t3, $sp       # calc address of sp
        
    lw    $t5, 0($t4)         # load array[i] in t5
    sw    $t5, 0($t2)         # store on the stack
      
    addi  $t1, $t1, 1         # i++
    blt   $t1, $t0, loop1     # branch until count < (row * col)

    # Calculating the size of the array we need in memory for sum
    sll   $t0, $a2, 2         # space for sum = col * 4
    sub   $sp, $sp, $t0       # stack pointer = stack pointer - space

    # Saving values from the s registers we're using onto the stack
    addi  $sp, $sp, -20       # Makes space on the stack for s0, s1, s2, s3, s4
    sw    $s0, 0($sp)
    sw    $s1, 4($sp)
    sw    $s2, 8($sp)
    sw    $s3, 12($sp)
    sw    $s4, 16($sp)

    # Setup for loop2 - using s registers
    addi  $s0, $sp, 16        # s0 = sum[0], moves up the stack past the old s-register values

    li    $s1, 0              # s1 = iteration number (output2 count, starting at 0)
    
    la    $t0, hold
    lw    $s2, 0($t0)         # s2 = max_col
 
    sll   $t0, $s2, 2         # multiplies max_col by 4, bit length of sum[0] 
    add   $s3, $s0, $t0       # s3 = board[0][0], sum[0] + (4 * max_col)

    move  $s4, $ra            # Stores the return address into s4

loop2:

    move  $a0, $s3            # loads the board[0][0] as a parameter to check
    li    $a1, 0              # loads the row at 0 as a parameter to check, always start at row 0
    move  $a2, $s1            # loads the output2 count as a parameter to check
    addi  $s1, 1              # increments the output2n count by 1

    # call check and store the results to our stack of results
    jal 	check
    
    # Stores the returned sum onto the stack for our sums
    sw 	$v0, 0($s0)		# stores the results for sum into sum[i]
    addi 	$s0, 4              	# increment the address for sum by 4 bytes
   
    bne   $s1, $s2, loop2 	# loop until we've passed check() each value from the starting row

    addi 	$s2, $s2, 1

    # setup for loop3 - figures out which starting square results in the largest max value
    move  	$t0, $zero          # zeros out $t0, which will hold the max value
    move  	$t1, $zero          # zeros out $t1, which will hold the starting output2 of the max value

    # Reset sum[0] and output2 count values since we added to them in the above iterations
    sll   $t2, $s2, 2     	# max_col * 4 (how large sum[] is)
    sub   $s0, $s0, $t2   	# sum[0] = sum[n] - max_col * 4
    li    $s1, 0         		# output2 count = 0 (what we're iterating with)

loop3:

    lw    $t3, 0($s0)          # loads sum[i]
 
    addi  $s0, 4               # adds for bytes to sum[] to get the next value in memory
    
    slt   $t4, $t3, $t0        # sum[i] < max_val
    
    bne   $t4, $zero, not_max  # if max_val !< sum[i], skip next two steps
    
    move  $t0, $t3             # max_val = sum[i]
    move  $t1, $s1             # output2 = count

not_max:

    addi  $s1, 1               # increments output2 count by 1
    bne   $s1, $s2, loop3      # loop until output2 count == max_col

    addi  $t1, $t1, -1         # Decrement col number by 1

    li    $v0, 4              # prep to print string
    la    $a0, output1         # load "The max value is "
    syscall

    li    $v0, 1              # prep to print integer
    move  $a0, $t0            # load the max value
    syscall

    li    $v0, 4              # prep to print string
    la    $a0, output2         # load " starting at output2 "
    syscall

    li    $v0, 1              # prep to print integer
    move  $a0, $t1            # load the starting output2
    syscall

    li    $v0, 4              # prep to print string
    la    $a0, newLine             # load newLine
    syscall

exit:

    # Caluclating the size of the board and sum on the stack
    la    $t0, row
    lw    $t0, 0($t0)
    mul   $t0, $t0, $s2        # max_col * max_row
    sll   $t0, $t0, 2          # size of board[], max_col * max_row * 4
    sll   $t1, $s2, 2          # size of sum[], max_col * 4

    # Restore our original return address
    move 	$ra, $s4

    # Restore the original s-values
    lw    $s0, 0($sp)
    lw    $s1, 4($sp)
    lw    $s2, 8($sp)
    lw    $s3, 12($sp)
    lw    $s4, 16($sp)

    # Clean the stack
    add   $t0, $t0, $t1        # adding the size of the sum[] and board[] to pop
    addi  $t0, $t0, 20         # adding the size of the s-registers to pop
    add   $sp, $sp, $t0        # pop stack (sum[] + board[] + s values[])

    # return to main
    jr 	$ra

check:

    # Saving values from the s registers we're using onto the stack
    addi  $sp, $sp, -32		# Makes space on the stack for all the s registers
    sw    $s0, 0($sp)
    sw    $s1, 4($sp)
    sw    $s2, 8($sp)
    sw    $s3, 12($sp)
    sw    $s4, 16($sp)
    sw    $s5, 20($sp)
    sw    $s6, 24($sp)
    sw    $s7, 28($sp)

    move  $s0, $a0             # s0 = board[0][0] address
    move  $s1, $a1             # s1 = row
    move  $s2, $a2             # s2 = col
    move  $s6, $ra             # s6 = return address

    # Find the value from the square we are currently at
    la    $t0, output2
    lw    $t0, 0($t0)
    mul   $t0, $t0, $s1        # row * max_col
    sll   $t0, $t0, 2          # (row * max_col) * 4 to move ahead to which row we are at

    sll   $t1, $s2, 2          # col * 4 to move to the right however many output2s we are at
    
    add   $t0, $t0, $t1        # the square we have moved to (row * max_col) + (col * 4)
    add   $t0, $s0, $t0        # adds the array value to the board address
    addi  $t0, $t0, 4          # value has been off 4 bytes for some reason
    lw    $s3, 0($t0)          # the value in the square we have moved to

    # Figure out if we are at base case, or should do a recursive call
    la    $t0, row
    lw    $t0, 0($t0)
    addi  $t0, $t0, -1
    bne   $t0, $s1, recursive  # if row != current row, skip the base case

    # Base Case (skipped if row != current row)
    move  $v0, $s3            	# return the value from the spot on the board we are currently at
    
    # Restores the original values of the s registers
    lw    $s0, 0($sp)
    lw    $s1, 4($sp)
    lw    $s2, 8($sp)
    lw    $s3, 12($sp)
    lw    $s4, 16($sp)
    lw    $s5, 20($sp)
    lw    $s6, 24($sp)
    lw    $s7, 28($sp) ww wv
    
    # Cleans the stack
    addi  $sp, $sp, 32		# Pops stack space made for s registers
    jr    $ra				# return to the previous recursive call 

recursive:

    addi  $s1, $s1, 1          # increment row by 1, because we'll move forward a row
    move  $s4, $zero           # sets our sum1 to 0 (in case it's never set)
    move  $s5, $zero           # sets our sum2 to 0 (in casse it's never set)	

    # if col - 1 >= 0
    addi  $t0, $s2, -1         # moves our output2 to the left by one
    blt   $t0, $zero, skip1    # if output2 - 1 >= 0, we need to call check to make that move
    move  $a1, $s1             # loads our incremented row as a parameter
    move  $a2, $t0             # loads our decremented output2 as a parameter
    jal   check                # make a recursive call to check
    add   $s4, $s3, $v0        # sum1 = value of current square + return value from recursive check call
    
skip1:

    # if col + 1 < max_col
    addi  $t0, $s2, 1          # moves our output2 to the right by one
    
    la 	$t6, hold
    lw 	$t6, 0($t6)		# gets the max number of output2s

    bge   $t0, $t6, skip2		# if output2 + 1 < max_col, we need to call check to make that move
    
    move  $a1, $s1             # loads our incremented row as a parameter
    move  $a2, $t0             # loads our decremented output2 as a parameter

    jal   check               	# make a recursive call to check
    add   $s5, $s3, $v0		# sum1 = value of current square + return value from recursive check call
    
skip2:

    #Figures out which sum from the two recursive calls is the largest, returns the largest sum
    move  $t0, $s4             # final sum = sum1 (col - 1 sum)
    slt   $t2, $s5, $s4        # checks if sum1 < sum2 (col + 1 sum)
    bne   $t2, $zero, skip3    # if col - 1 sum !< col + 1 sum, skip the next line
    move  $t0, $s5             # final sum = sum2 (col + 1 sum)

skip3:

    #Pop this recursive stackframe
    move  $ra, $s6             # put the apporpriate return address into $ra
    
    # Restores the original values of the s registers
    lw    $s0, 0($sp)
    lw    $s1, 4($sp)
    lw    $s2, 8($sp)
    lw    $s3, 12($sp)
    lw    $s4, 16($sp)
    lw    $s5, 20($sp)
    lw    $s6, 24($sp)
    lw    $s7, 28($sp)
    
    # Cleans the stack
    addi  $sp, $sp, 32		# Pops stack space made for s registers

    move  $v0, $t0             # return finalSum
    jr    $ra                  # Returning to the loaded address
