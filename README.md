
# DataFrame Library for Java

## Overview

The Java DataFrame Library is a comprehensive toolset designed for data manipulation and analysis, akin to the functionality found in Python's pandas library and R's data frames but tailored for the Java ecosystem. It enables efficient handling of tabular data, offering a suite of operations for data processing, analysis, and visualization.

## Features

- **Core DataFrame Operations (`DataFrame.java`)**: Create, manipulate, and query data frames. Supports CRUD operations on rows and columns, data filtering, and basic transformations.
- **Data Vector Manipulation (`DataVector.java`, `DoubleDataVector.java`)**: Manage columnar data efficiently. `DataVector.java` is generic, supporting various data types, while `DoubleDataVector.java` is optimized for double precision floating-point numbers.
- **Statistical Analysis (`DataFrameStatistics.java`)**: Perform descriptive statistics, including mean, median, variance, and standard deviation calculations, directly on data frames.
- **Type-Specific Data Frames (`DoubleDataFrame.java`)**: Specialized implementation for numerical data analysis, improving performance and memory usage for datasets predominantly containing double values.
- **Utility Classes (`RandomTools.java`, `FileTools.java`)**: Facilitate common tasks such as generating random datasets for testing and handling file-based data input/output operations.

## Getting Started

### Prerequisites

Ensure Java JDK 8 or higher is installed on your system.

### Installation

Clone the repository and include it in your project's build path. If using an IDE, import the project directly.

### Quick Start Guide

```java
import your.library.namespace.DataFrame;

public class Example {
    public static void main(String[] args) {
        DataFrame df = new DataFrame();
        // Example: Load data from a CSV file
        df.loadCsv("path/to/your/data.csv");
        // Perform operations
        df.describe(); // Summarize the data
    }
}

## Acknowledgments

Huge appreciation to Paul Boulman for providing me with the interface that significantly contributed to the development of this project. His insights and support were invaluable

