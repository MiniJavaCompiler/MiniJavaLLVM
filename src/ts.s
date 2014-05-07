	.file	"ts.j"
	.globl	Main_main
System_0:
	.long	4
Expr_eval:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$0,%eax
	movl	%ebp,%esp
	popl	%ebp
	ret
Expr_0:
	.long	4
	.long	Expr_eval
IntExpr_make:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	$IntExpr_0
	call	new_object
	addl	$4,%esp
	movl	%eax,-4(%ebp)
	movl	8(%ebp),%eax
	movl	-4(%ebp),%ecx
	movl	%eax,4(%ecx)
	movl	-4(%ebp),%eax
	movl	%ebp,%esp
	popl	%ebp
	ret
IntExpr_eval:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	4(%eax),%eax
	movl	%ebp,%esp
	popl	%ebp
	ret
IntExpr_0:
	.long	8
	.long	IntExpr_eval
AddExpr_make:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	$AddExpr_0
	call	new_object
	addl	$4,%esp
	movl	%eax,-4(%ebp)
	movl	12(%ebp),%eax
	movl	-4(%ebp),%ecx
	movl	%eax,8(%ecx)
	movl	8(%ebp),%eax
	movl	-4(%ebp),%ecx
	movl	%eax,4(%ecx)
	movl	-4(%ebp),%eax
	movl	%ebp,%esp
	popl	%ebp
	ret
AddExpr_eval:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	8(%eax),%eax
	pushl	%eax
	movl	(%eax),%eax
	movl	4(%eax),%eax
	call	*%eax
	addl	$4,%esp
	pushl	%eax
	movl	8(%ebp),%eax
	movl	4(%eax),%eax
	pushl	%eax
	movl	(%eax),%eax
	movl	4(%eax),%eax
	call	*%eax
	movl	%eax,%ecx
	addl	$4,%esp
	popl	%eax
	addl	%ecx,%eax
	movl	%ebp,%esp
	popl	%ebp
	ret
AddExpr_0:
	.long	12
	.long	AddExpr_eval
Main_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	movl	$1,%eax
	pushl	%eax
	call	IntExpr_make
	addl	$4,%esp
	pushl	%eax
	movl	$2,%eax
	pushl	%eax
	call	IntExpr_make
	addl	$4,%esp
	pushl	%eax
	call	AddExpr_make
	addl	$8,%esp
	movl	%eax,-4(%ebp)
	movl	-4(%ebp),%eax
	pushl	%eax
	movl	-4(%ebp),%eax
	pushl	%eax
	call	AddExpr_make
	addl	$8,%esp
	movl	%eax,-4(%ebp)
	movl	-4(%ebp),%eax
	pushl	%eax
	movl	(%eax),%eax
	movl	4(%eax),%eax
	call	*%eax
	addl	$4,%esp
	pushl	%eax
	call	System_out
	addl	$4,%esp
	movl	%ebp,%esp
	popl	%ebp
	ret
Main_0:
	.long	4
