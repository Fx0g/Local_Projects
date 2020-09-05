#fibonacci series

def find_fib(n):
    a = 1
    b = 1
    fib = [1,1]
    for i in range(n):
        c = a + b
        fib.append(c)
        a = b
        b = c
    return fib

print(find_fib(8))