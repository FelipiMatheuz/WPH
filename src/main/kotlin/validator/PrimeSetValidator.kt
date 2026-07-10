package validator

import model.domain.prime.PrimePart
import model.domain.prime.PrimeSet

class PrimeSetValidator {

    fun validate(primeSets: List<PrimeSet>) {
        require(primeSets.isNotEmpty()) {
            "Prime Sets list is empty."
        }

        validateUniqueIds(primeSets)
        validateNames(primeSets)
        validateComponents(primeSets)
        validateReferences(primeSets)
        validateCircularDependencies(primeSets)
    }

    private fun validateUniqueIds(primeSets: List<PrimeSet>) {
        val duplicates = primeSets
            .groupBy { it.id }
            .filterValues { it.size > 1 }

        require(duplicates.isEmpty()) {
            "Duplicate PrimeSet ids: ${duplicates.keys}"
        }
    }

    private fun validateNames(primeSets: List<PrimeSet>) {
        primeSets.forEach {

            require(it.name.isNotBlank()) {
                "PrimeSet '${it.id}' has empty name."
            }

            require(it.components.isNotEmpty()) {
                "PrimeSet '${it.id}' has no components."
            }
        }
    }

    private fun validateComponents(primeSets: List<PrimeSet>) {

        primeSets.forEach { set ->

            set.components.forEach { component ->

                require(component.quantity > 0) {
                    "Component '${component.id}' of '${set.name}' has invalid quantity."
                }

                require(component.id.isNotBlank()) {
                    "PrimeSet '${set.name}' contains empty component id."
                }
            }
        }
    }

    private fun validateReferences(primeSets: List<PrimeSet>) {

        val ids = primeSets
            .map { it.id }
            .toSet()

        primeSets.forEach { set ->

            set.components
                .filter { it.part == PrimePart.PRIME_SET }
                .forEach {
                    require(it.id in ids) {
                        "PrimeSet '${set.name}' references unknown PrimeSet '${it.id}'."
                    }
                }
        }
    }

    private fun validateCircularDependencies(
        primeSets: List<PrimeSet>
    ) {
        val graph = primeSets.associateBy { it.id }

        val visiting = mutableSetOf<String>()
        val visited = mutableSetOf<String>()

        graph.keys.forEach {
            dfs(it, graph, visiting, visited)
        }
    }

    private fun dfs(
        id: String,
        graph: Map<String, PrimeSet>,
        visiting: MutableSet<String>,
        visited: MutableSet<String>
    ) {

        if (id in visited)
            return

        require(id !in visiting) {
            "Circular dependency detected involving PrimeSet '$id'"
        }

        visiting += id

        graph[id]
            ?.components
            ?.filter { it.part == PrimePart.PRIME_SET }
            ?.forEach {

                dfs(
                    id = it.id,
                    graph = graph,
                    visiting = visiting,
                    visited = visited
                )
            }

        visiting -= id
        visited += id
    }
}



