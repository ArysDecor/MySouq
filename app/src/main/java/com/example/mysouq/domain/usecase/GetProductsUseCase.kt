package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.model.Product
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Result<List<Product>>> {
        return repository.observeAll()
    }
}
