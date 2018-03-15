#!usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Knapsack problem.

Assume that the values are all integer.

Another dynamic programming algorithm:
Let S(i, x) be the optimal solution for the subproblem among the first i items
and target value we are striving for x, i.e., the minimum total weight subject
to getting a total value >= x.
Consider whether item-i is in S:
1. item-i is NOT in S:
   => S must be optimal among only the first (i - 1) items and target value x.
   => S = the optimal solution among the first (i - 1) items and target value x
2. item-i is in S:
   => {S - item-i} must be optimal among only the first (i - 1) items and target
      value (x - v_i).
   => S = the optimal solution among the first (i - 1) items and target value
          (x - v_i) + item-i
i.e.,
S(i, x) = max{S(i - 1, x), S(i - 1, x - v_i) + w_i}
"""

__author__ = 'Ziang Lu'

INFINITY = 1000000.0


class IllegalArgumentError(ValueError):
    pass


def knapsack(vals, weights, cap):
    """
    Solves the knapsack problem of the items with the given values and weights,
    and the given capacity, in an improved bottom-up way.
    :param vals: list[int]
    :param weights: list[float]
    :param cap: float
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(weights) == 0 \
            or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'null or empty.')
    # Check whether the input capacity is non-negative
    if cap < 0.0:
        raise IllegalArgumentError('The input capacity should be non-negative.')

    n = len(vals)
    val_sum = sum(vals)
    # Initialization
    subproblems = [[0] * (val_sum + 1) for i in range(n)]
    for x in range(val_sum + 1):
        if vals[0] >= x:
            subproblems[0][x] = weights[0]
        else:
            subproblems[0][x] = INFINITY
    # Bottom-up calculation
    for item in range(1, n):
        for x in range(val_sum + 1):
            result_without_curr = subproblems[item - 1][x]
            result_with_curr = weights[item]
            if vals[item] < x:
                result_with_curr += subproblems[item - 1][x - vals[item]]
            subproblems[item][x] = min(result_without_curr, result_with_curr)
    return _reconstruct(vals, weights, cap, subproblems=subproblems)
    # Overall running time complexity: O(n sum(v)) <= O(n n v_max) =
    # O(n ^ 2 v_max)


def _reconstruct(vals, weights, cap, subproblems):
    """
    Private helper function to find the optimal solution itself and reconstruct
    the included items according to the optimal solution using backtracking.
    :param vals: list[int]
    :param weights: list[float]
    :param cap: float
    :param subproblems: list[list[float]]
    :return: set{int}
    """
    # Find the optimal solution itself
    val_sum = sum(vals)
    max_total_val, last_item = 0, -1
    for x in range(val_sum, -1, -1):
        found = False
        for item in range(len(vals)):
            if subproblems[item][x] <= cap:
                max_total_val, last_item = x, item
                found = True
                break
        if found:
            break

    # Reconstruct the included items from the optimal solution
    included_items = set()
    curr_item, curr_target_val, curr_cap = last_item, max_total_val, cap
    while curr_item >= 1:
        result_without_curr = subproblems[curr_item - 1][curr_target_val]
        result_with_curr = weights[curr_item]
        if vals[curr_item] < curr_target_val:
            result_with_curr += \
                subproblems[curr_item - 1][curr_target_val - vals[curr_item]]
        if result_without_curr > result_with_curr \
                and weights[curr_item] <= curr_cap:
            # Case 2: The current item is included.
            included_items.add(curr_item)
            curr_target_val -= vals[curr_item]
            curr_cap -= weights[curr_item]
        curr_item -= 1
    if vals[0] >= curr_target_val and weights[0] <= curr_cap:
        included_items.add(0)
    return included_items
    # Running time complexity: O(n)


def knapsack_with_dynamic_programming_heuristic(vals, weights, cap, epsilon):
    """
    Solves the knapsack problem of the items with the given values and weights,
    and the given capacity, using a dynamic programming-based heuristic with the
    given tolerant error rate.
    :param vals: list[float]
    :param weights: list[float]
    :param cap: float
    :param epsilon: float
    :return: set{int}
    """
    # Check whether the input arrays are None or empty
    if vals is None or len(weights) == 0 \
            or weights is None or len(weights) == 0:
        raise IllegalArgumentError('The input values and weights should not be '
                                   'null or empty.')
    # Check whether the input capacity is non-negative
    if cap < 0.0:
        raise IllegalArgumentError('The input capacity should be non-negative.')
    # Check whether the input tolerant error rate is in (0, 1)
    if epsilon <= 0.0 or epsilon >= 1.0:
        raise IllegalArgumentError('The input tolerant error rate should be in '
                                   '(0, 1).')

    # Refer to the documentation for the choice of m
    max_val, n = max(vals), len(vals)
    m = epsilon * max_val / n
    # Perform transformation
    transformed_vals = [int(val / m) for val in vals]

    # Feed the rounded values, the original weights and the knapsack capacity
    # into the above algorithm, and get the included items in the optimal
    # solution
    return knapsack(transformed_vals, weights, cap)
    # Overall running time complexity: O(n^3 / epsilon)